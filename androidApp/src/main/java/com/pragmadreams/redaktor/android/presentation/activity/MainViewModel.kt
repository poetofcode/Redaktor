package com.pragmadreams.redaktor.android.presentation.activity

import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.base.BaseViewModel
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.base.State
import com.pragmadreams.redaktor.android.domain.UseCases
import com.pragmadreams.redaktor.android.navigation.NavigationEffect
import com.pragmadreams.redaktor.android.navigation.RootScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class PageState(
    val textState: String = "Page UI state"
) : State

class MainViewModel : BaseViewModel<PageState, PageIntent>() {

    private val useCase = UseCases.editorUseCase

    init {
        useCase.fetchPageById("1")
            .onEach {

                val page = it

                println("mylog Page: $it")
            }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: PageIntent) {
        when (intent) {
            PageIntent.SomeUserIntent -> {
                println("mylog Intent: $intent")

                updateState { copy(
                    textState = state.value.textState + " NEW"
                ) }
            }
            PageIntent.ToSampleScreen -> {
                offerEffect(NavigationEffect.Navigate(RootScreen.SampleScreen))
            }
        }
    }

    override fun createState(): PageState = PageState()

}

sealed class PageIntent : Intent {

    object SomeUserIntent : PageIntent()
    object ToSampleScreen : PageIntent()

}