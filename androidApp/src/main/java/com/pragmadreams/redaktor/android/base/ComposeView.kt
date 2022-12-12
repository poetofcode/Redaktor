package com.pragmadreams.redaktor.android.base

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

abstract class ComposeView<StateType : State, IntentType : Intent> {

    private val emptyFunc: (IntentType) -> Unit = {}
    private lateinit var state : StateType

    protected val LocalIntent = staticCompositionLocalOf { emptyFunc }
    protected val LocalState = staticCompositionLocalOf { state }


    @Composable
    open fun WrappedView(state: StateType, offerIntent: (IntentType) -> Unit) {
        this.state = state
        CompositionLocalProvider(
            LocalIntent provides offerIntent,
            LocalState provides state
        ) {
            MaterialTheme {
                ContentView()
            }
        }
    }

    @Composable
    protected abstract fun ContentView()

}