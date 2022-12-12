package com.pragmadreams.redaktor.android.presentation.activity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.domain.UseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {

    private val useCase = UseCases.editorUseCase

    val textState: MutableState<String> = mutableStateOf("")

    init {
        useCase.fetchPageById("1")
            .onEach {

                val page = it

                println("mylog Page: $it")
            }
            .launchIn(viewModelScope)

        textState.value = "Passed from ViewModel"
    }

    fun handleUiIntent(intent: PageUiIntent) {
        when (intent) {
            PageUiIntent.SomeUserIntent -> {
                println("mylog Intent: $intent")
            }
        }
    }

}

sealed class PageUiIntent : Intent {

    object SomeUserIntent : PageUiIntent()

}