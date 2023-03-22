package com.pragmadreams.redaktor.android.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.shared.Hub
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel<S: State, I: Intent> : ViewModel() {

    val state: MutableState<S> by lazy { mutableStateOf(createState()) }

    @Inject
    lateinit var hub : Hub

    abstract fun handleIntent(intent: I)
    abstract fun createState(): S

    fun updateState(updater: S.() -> S) {
        state.value = state.value.updater()
    }

    fun offerEffect(effect: Effect) {
        viewModelScope.launch { hub.effectFlow.emit(effect) }
    }
}