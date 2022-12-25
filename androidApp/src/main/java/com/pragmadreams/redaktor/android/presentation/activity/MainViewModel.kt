package com.pragmadreams.redaktor.android.presentation.activity

import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.base.BaseViewModel
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.base.State
import com.pragmadreams.redaktor.android.domain.UseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class PageUiState(
    val textState: String = "Page UI state"
) : State

class MainViewModel : BaseViewModel<PageUiState, PageUiIntent>() {

    private val useCase = UseCases.editorUseCase

    init {
        useCase.fetchPageById("1")
            .onEach {

                val page = it

                println("mylog Page: $it")
            }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: PageUiIntent) {
        when (intent) {
            PageUiIntent.SomeUserIntent -> {
                println("mylog Intent: $intent")

                updateState { copy(
                    textState = state.value.textState + " NEW"
                ) }
            }
        }
    }

    override fun createState(): PageUiState = PageUiState()

}

sealed class PageUiIntent : Intent {

    object SomeUserIntent : PageUiIntent()

}