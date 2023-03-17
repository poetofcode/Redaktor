package com.pragmadreams.redaktor.android.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.pragmadreams.redaktor.android.presentation.screen.list.CatalogView
import com.pragmadreams.redaktor.android.presentation.screen.list.CatalogViewModel
import com.pragmadreams.redaktor.android.presentation.screen.page.PageView
import com.pragmadreams.redaktor.android.presentation.screen.page.PageViewModel
import com.pragmadreams.redaktor.android.util.composable

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = RootScreen.CatalogScreen.route) {
        composable(RootScreen.PageScreen) {
            PageView().Content(hiltViewModel<PageViewModel>())
        }

        composable(
            route = RootScreen.SampleScreen,
            arguments = listOf(navArgument("pageId") { type = NavType.StringType })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Sample screen")
            }
        }

        composable(route = RootScreen.CatalogScreen) {
            CatalogView().Content(hiltViewModel<CatalogViewModel>())
        }
    }
}

sealed class RootScreen(override val route: String) : Screen(route) {
    object PageScreen : RootScreen("/page_screen/{pageId}")

    object SampleScreen : RootScreen("/page/sample")

    object CatalogScreen : RootScreen("/page_screen/catalog")
}