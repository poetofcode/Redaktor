package com.pragmadreams.redaktor.android.presentation.screen.page

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

class PageViewModel : BaseViewModel<PageState, PageIntent>() {

    private val useCase = UseCases.editorUseCase

    init {
        useCase.fetchPageById("1")
            .onEach {
                val elementsUI = fromElementsApi(it.elements)
                updateState { copy(
                    elements = elementsUI
                ) }
            }
            .catch { e -> e.printStackTrace() }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: PageIntent) {
        when (intent) {
            PageIntent.SomeUserIntent -> {
                /*
                println("mylog Intent: $intent")
                updateState { copy(
                    textState = state.value.textState + " NEW"
                ) }
                */
            }
            PageIntent.ToSampleScreen -> {
                offerEffect(NavigationEffect.Navigate(RootScreen.SampleScreen))
            }
            PageIntent.OnFinishEditModeClick -> {
                updateState { copy(mode = PageMode.VIEW) }
            }
            PageIntent.OnStartEditModeClick -> {
                updateState { copy(mode = PageMode.SELECT) }
            }
            is PageIntent.OnActionClick -> {
                handleActionClick(intent.element, intent.action)
            }
            PageIntent.OnApplyElementChangesClick -> {
                // TODO
            }
            PageIntent.OnDiscardChangesElementClick -> {
                updateState { copy(
                    mode = PageMode.SELECT,
                    editableElement = null,
                ) }
            }
            is PageIntent.OnEditableElementChanged -> {
                updateState { copy(editableElement = intent.updatedElement) }
            }
        }
    }

    private fun handleActionClick(element: ElementUI, action: ActionUI) {
        when (action) {
            ActionUI.Delete -> { /* TODO */ }
            ActionUI.Edit -> {
                updateState { copy(
                    mode = PageMode.EDIT,
                    editableElement = element,
                ) }
            }
        }
    }

    override fun createState(): PageState = PageState()

    private fun fromElementsApi(items: List<Element>) : List<ElementUI> {
        return items.map {
            when (val element = it) {
                is TextElement -> {
                    ElementUI.Text(text = element.text, id = element.id)
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
    data class OnActionClick(val element: ElementUI, val action: ActionUI) : PageIntent()
    data class OnEditableElementChanged(val updatedElement: ElementUI) : PageIntent()
    object SomeUserIntent : PageIntent()
    object ToSampleScreen : PageIntent()
    object OnStartEditModeClick : PageIntent()
    object OnFinishEditModeClick : PageIntent()
    object OnApplyElementChangesClick : PageIntent()
    object OnDiscardChangesElementClick : PageIntent()
}

data class PageState(
    val textState: String = "Page UI state",
    val elements: List<ElementUI> = emptyList(),
    val mode: PageMode = PageMode.VIEW,
    val editableElement: ElementUI? = null,
) : State


sealed class ActionUI {
    object Edit : ActionUI()
    object Delete : ActionUI()

    companion object {
        val BY_DEFAULT = listOf(Edit, Delete)
    }
}

sealed class ElementUI(open val id: String) {

    data class Text(
        override val id: String,
        val text: String,
        val actions: List<ActionUI> = ActionUI.BY_DEFAULT
    ) : ElementUI(id)

}


enum class PageMode {
    VIEW, SELECT, EDIT
}