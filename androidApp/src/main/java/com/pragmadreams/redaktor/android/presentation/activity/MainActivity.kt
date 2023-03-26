package com.pragmadreams.redaktor.android.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pragmadreams.redaktor.android.navigation.NavigationEffect
import com.pragmadreams.redaktor.android.navigation.RootNavGraph
import com.pragmadreams.redaktor.android.shared.Hub
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var hub : Hub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController().apply {
                collectNavigationEvents(this)
            }
            RootNavGraph(navController = navController)
        }
    }

    private fun collectNavigationEvents(navHostController: NavHostController) = lifecycleScope.launch {
        hub.effectFlow.collect { effect ->
            if (effect is NavigationEffect) {
                when (effect) {
                    is NavigationEffect.Navigate -> navHostController.navigate(
                        effect.screen.buildTargetRoute()
                    )
                    NavigationEffect.NavigateUp -> navHostController.navigateUp()
                }
            }
        }
    }

}

