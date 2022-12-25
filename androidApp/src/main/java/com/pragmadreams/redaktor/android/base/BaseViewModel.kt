package com.pragmadreams.redaktor.android.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<S: State, I: Intent> : ViewModel() {

    val state: MutableState<S> by lazy { mutableStateOf(createState()) }

    abstract fun handleIntent(intent: I)
    abstract fun createState(): S

    fun updateState(updater: S.() -> S) {
        state.value = state.value.updater()
    }

}