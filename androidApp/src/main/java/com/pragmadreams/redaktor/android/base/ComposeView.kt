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

    private lateinit var state : StateType
    private val emptyFunc: (IntentType) -> Unit = {}

    protected val LocalIntent = staticCompositionLocalOf { emptyFunc }
    protected val LocalState = staticCompositionLocalOf { state }

    @Composable
    fun Content(state: StateType, offerIntent: (IntentType) -> Unit = {}) {
        this.state = state
        CompositionLocalProvider(
            LocalIntent provides offerIntent,
            LocalState provides state
        ) {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                    content = { Layout() }
                )
            }
        }
    }

    @Composable
    fun Content(viewModel: BaseViewModel<StateType, IntentType>) {
        Content(state = viewModel.state.value, offerIntent = viewModel::handleIntent)
    }

    @Composable
    protected abstract fun Layout()

}