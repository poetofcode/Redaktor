package com.pragmadreams.redaktor.android.presentation.activity

import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.base.BaseViewModel
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.base.State
import com.pragmadreams.redaktor.android.domain.UseCases
import com.pragmadreams.redaktor.android.navigation.NavigationEffect
import com.pragmadreams.redaktor.android.navigation.RootScreen
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.TextElement
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : BaseViewModel<PageState, PageIntent>() {

    private val useCase = UseCases.editorUseCase

    init {
        useCase.fetchPageById("1")
            .onEach {
                val elementsUI = fromElementsApi(it.elements)
                updateState { copy(
                    elements = elementsUI
                ) }
            }
            .catch { e ->
                e.printStackTrace()
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
            PageIntent.OnFinishEditModeClick -> {

            }
            PageIntent.OnStartEditModeClick -> {

            }
        }
    }

    override fun createState(): PageState = PageState()

    private fun fromElementsApi(items: List<Element>) : List<ElementUI> {
        return items.map {
            when (val element = it) {
                is TextElement -> {
                    ElementUI(text = element.text)
                }

                else -> {
                    // TODO remove emptyList and remove single item from list
                    return emptyList()
                }
            }
        }
    }

}

sealed class PageIntent : Intent {

    object SomeUserIntent : PageIntent()
    object ToSampleScreen : PageIntent()
    object OnStartEditModeClick : PageIntent()
    object OnFinishEditModeClick : PageIntent()

}

data class PageState(
    val textState: String = "Page UI state",
    val elements: List<ElementUI> = emptyList(),
    val mode: PageMode = PageMode.VIEW,
) : State

data class ElementUI(
    val text: String,
)

enum class PageMode {
    VIEW, EDIT, LOADING
}