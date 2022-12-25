package com.pragmadreams.redaktor.android.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val vm: MainViewModel = viewModel()

            MainView().WrappedView(
                state = vm.state.value,
                offerIntent = vm::handleIntent
            )
        }
    }

}

