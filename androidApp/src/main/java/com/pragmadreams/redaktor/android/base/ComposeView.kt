package com.pragmadreams.redaktor.android.base

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.pragmadreams.redaktor.android.MyApplicationTheme

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
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                    content = { ContentView() }
                )
            }
        }
    }

    @Composable
    protected abstract fun ContentView()

}