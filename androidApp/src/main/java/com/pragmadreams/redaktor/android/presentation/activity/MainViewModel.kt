package com.pragmadreams.redaktor.android.presentation.activity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.domain.UseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {

    private val useCase = UseCases.editorUseCase

    val textState: MutableState<String> = mutableStateOf("")

    init {
        useCase.fetchPageById("1")
            .onEach {
                println("mylog Page: $it")
            }
            .launchIn(viewModelScope)

        textState.value = "Passed from ViewModel"
    }

}