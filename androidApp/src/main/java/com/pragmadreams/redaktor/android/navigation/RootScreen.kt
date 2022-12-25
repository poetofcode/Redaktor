package com.pragmadreams.redaktor.android.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pragmadreams.redaktor.android.presentation.activity.MainView
import com.pragmadreams.redaktor.android.util.composable

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = RootScreen.PageScreen.route) {
        composable(RootScreen.PageScreen) {
            MainView().Content()
        }
    }
}

sealed class RootScreen(override val route: String) : Screen(route) {
    object PageScreen : RootScreen("/page_screen")
}