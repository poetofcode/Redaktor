package com.pragmadreams.redaktor.android.presentation.screen.page

import androidx.lifecycle.*
import com.pragmadreams.redaktor.android.base.BaseViewModel
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.base.State
import com.pragmadreams.redaktor.android.domain.UseCases
import com.pragmadreams.redaktor.android.navigation.NavigationEffect
import com.pragmadreams.redaktor.android.navigation.RootScreen
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.LinkElement
import com.pragmadreams.redaktor.entity.TextElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PageViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
) : BaseViewModel<PageState, PageIntent>() {

    private val pageId: String = savedStateHandle["pageId"] ?: getStartId()

    private fun getStartId(): String {
        // TODO get from permanent storage
        return "1"
    }

    private val useCase = UseCases.editorUseCase

    init {
        fetchPageData()
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
                updateState { copy(mode = PageMode.View) }
            }
            PageIntent.OnStartEditModeClick -> {
                updateState { copy(mode = PageMode.Select) }
            }
            is PageIntent.OnActionClick -> {
                handleActionClick(intent.element, intent.action)
            }
            PageIntent.OnApplyElementChangesClick -> {
                applyElementChanges()
            }
            PageIntent.OnDiscardChangesElementClick -> {
                updateState {
                    copy(
                        mode = PageMode.Select,
                    )
                }
            }
            is PageIntent.OnEditableElementChanged -> {
                updateState {
                    copy(
                        mode = PageMode.Edit(intent.updatedElement)
                    )
                }
            }
            is PageIntent.OnElementClick -> {
                when (val element = intent.element) {
                    is ElementUI.Link -> {
                        element.relatedPageId?.let { pageId ->
                            println("mylog navigate to PageId = $pageId")
                            offerEffect(NavigationEffect.Navigate(
                                RootScreen.PageScreen.withArguments("pageId" to pageId)
                            ))
                        }
                    }

                    else -> Unit
                }
            }

            PageIntent.OnAddNewElementClick -> {
                onAddNewElementClick()
            }
        }
    }

    private fun applyElementChanges() {
        when (val mode = state.value.mode) {
            is PageMode.Edit -> {
                val editableElement = mode.element
                useCase.createOrUpdateElement(pageId, toElementApi(editableElement))
                    .onEach {
                        fetchPageData()
                        updateState {
                            copy(
                                mode = PageMode.Select,
                            )
                        }
                    }
                    .catch { e -> e.printStackTrace() }
                    .launchIn(viewModelScope)
            }
            else -> return
        }
    }

    private fun onAddNewElementClick() {
        println("mylog onAddNewElementClick() addNewElementToPage")
        addNewElementToPage(TextElement.createEmpty())
    }

    private fun addNewElementToPage(element: Element) {
        println("mylog onAddNewElementClick(), element.id = ${element.id}")
        useCase.createOrUpdateElement(pageId, element)
            .onEach {
                fetchPageData()
                // TODO открывать сразу экран редактирования элемента после доабвления
//                updateState {
//                    val elementUi = elements.firstOrNull { it.id == element.id }
//                    copy(
//                        mode = PageMode.Edit(elementUi ?: return@updateState state.value),
//                    )
//                }
            }
            .catch { e -> e.printStackTrace() }
            .launchIn(viewModelScope)
    }

    private fun fetchPageData() {
        useCase.fetchPageById(pageId)
            .onEach {
                val elementsUI = fromElementsApi(it.elements)
                updateState { copy(elements = elementsUI) }
            }
            .catch { e -> e.printStackTrace() }
            .launchIn(viewModelScope)
    }

    private fun handleActionClick(element: ElementUI, action: ActionUI) {
        when (action) {
            ActionUI.Delete -> { /* TODO */
            }
            ActionUI.Edit -> {
                updateState {
                    copy(
                        mode = PageMode.Edit(element),
                    )
                }
            }
            ActionUI.BindLink -> {
                // TODO navigate to page-list/choose-page screen
            }
        }
    }

    override fun createState(): PageState = PageState()

    private fun fromElementsApi(items: List<Element>): List<ElementUI> {
        return items.mapNotNull {
            when (val element = it) {
                is TextElement -> {
                    ElementUI.Text(text = element.text, id = element.id)
                }

                is LinkElement -> {
                    ElementUI.Link(
                        text = element.text,
                        id = element.id,
                        relatedPageId = element.relatedPageId
                    )
                }
                else -> null
            }
        }
    }

    private fun toElementApi(elementUi: ElementUI): Element {
        return when (elementUi) {
            is ElementUI.Text -> TextElement(
                id = elementUi.id,
                text = elementUi.text,
            )
            is ElementUI.Link -> LinkElement(
                id = elementUi.id,
                text = elementUi.text,
                relatedPageId = elementUi.relatedPageId,
            )
        }
    }

}

sealed class PageIntent : Intent {
    data class OnActionClick(val element: ElementUI, val action: ActionUI) : PageIntent()
    data class OnEditableElementChanged(val updatedElement: ElementUI) : PageIntent()
    data class OnElementClick(val element: ElementUI) : PageIntent()
    object SomeUserIntent : PageIntent()
    object ToSampleScreen : PageIntent()
    object OnStartEditModeClick : PageIntent()
    object OnFinishEditModeClick : PageIntent()
    object OnApplyElementChangesClick : PageIntent()
    object OnDiscardChangesElementClick : PageIntent()
    object OnAddNewElementClick : PageIntent()
}

data class PageState(
    val textState: String = "Page UI state",
    val elements: List<ElementUI> = emptyList(),
    val mode: PageMode = PageMode.View,
) : State


sealed class ActionUI {
    object Edit : ActionUI()
    object Delete : ActionUI()
    object BindLink : ActionUI()

    companion object {
        val BY_DEFAULT: List<ActionUI> = emptyList()
    }
}

sealed class ElementUI(
    open val id: String,
    open val actions: List<ActionUI> = ActionUI.BY_DEFAULT,
) {

    data class Text(
        override val id: String,
        val text: String,
    ) : ElementUI(id)

    data class Link(
        override val id: String,
        val text: String,
        val relatedPageId: String?,
    ) : ElementUI(id) {
        override val actions: List<ActionUI> = listOf(ActionUI.BindLink)

        val isBound: Boolean
            get() {
                return relatedPageId != null
            }
    }

}

sealed class PageMode {
    object View : PageMode()
    object Select : PageMode()
    data class Edit(val element: ElementUI) : PageMode()
}