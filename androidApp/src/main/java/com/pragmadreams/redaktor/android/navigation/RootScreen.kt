package com.pragmadreams.redaktor.android.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pragmadreams.redaktor.android.presentation.screen.page.PageView
import com.pragmadreams.redaktor.android.presentation.screen.page.PageViewModel
import com.pragmadreams.redaktor.android.util.composable


@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = RootScreen.PageScreen.route) {
        composable(RootScreen.PageScreen) {
            PageView().Content(viewModel<PageViewModel>())
        }

        composable(RootScreen.SampleScreen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Sample screen")
            }
        }
    }
}

sealed class RootScreen(override val route: String) : Screen(route) {
    object PageScreen : RootScreen("/page_screen")

    object SampleScreen : RootScreen("/page/sample")
}