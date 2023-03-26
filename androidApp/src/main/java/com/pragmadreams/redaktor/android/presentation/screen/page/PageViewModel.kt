package com.pragmadreams.redaktor.android.presentation.screen.page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.base.BaseViewModel
import com.pragmadreams.redaktor.android.base.Effect
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.base.State
import com.pragmadreams.redaktor.android.domain.model.ActionUI
import com.pragmadreams.redaktor.android.domain.model.ElementUI
import com.pragmadreams.redaktor.android.domain.model.PageMode
import com.pragmadreams.redaktor.android.domain.model.PageUI
import com.pragmadreams.redaktor.android.navigation.NavigationEffect
import com.pragmadreams.redaktor.android.navigation.RootScreen
import com.pragmadreams.redaktor.android.presentation.screen.page.misc.ElementType
import com.pragmadreams.redaktor.android.presentation.screen.page.misc.OnPagePickedEffect
import com.pragmadreams.redaktor.domain.usecase.EditorUseCase
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.LinkElement
import com.pragmadreams.redaktor.entity.Page
import com.pragmadreams.redaktor.entity.TextElement
import com.pragmadreams.redaktor.util.swap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val editorUseCase: EditorUseCase,
) : BaseViewModel<PageState, PageIntent>() {

    private val pageId: String? = savedStateHandle["pageId"]

    init {
        fetchPageData()
    }

    override fun handleIntent(intent: PageIntent) {
        when (intent) {
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
                        element.relatedPage?.let { page ->
                            offerEffect(NavigationEffect.Navigate(
                                RootScreen.PageScreen.withArguments("pageId" to page.id)
                            ))
                        }
                    }

                    else -> Unit
                }
            }

            PageIntent.OnAddNewElementClick -> {
                onAddNewElementClick()
            }
            is PageIntent.OnReorderListElement -> {
                editorUseCase.reorderElements(
                    pageId = state.value.pageId ?: return,
                    firstElementId = state.value.elements[intent.oldPosition].id,
                    secondElementId = state.value.elements[intent.newPosition].id,
                )
                    .catch { e ->
                        e.printStackTrace()
                        fetchPageData()
                    }
                    .launchIn(viewModelScope)

                updateState {
                    copy(
                        elements = elements.swap(intent.oldPosition, intent.newPosition),
                        draggableIndex = intent.newPosition,
                    )
                }
            }
            PageIntent.OnFinishDragging -> updateState { copy(draggableIndex = null) }
            is PageIntent.OnStartDragging -> updateState { copy(draggableIndex = intent.itemIndex) }

            is PageIntent.OnSelectElementType -> {
                updateState { copy(elementType = intent.elementType) }
            }
        }
    }

    override fun handleIntermediateEffect(effect: Effect) {
        when (effect) {
            is OnPagePickedEffect -> {
                val currentMode = state.value.mode
                if (currentMode !is PageMode.Edit) {
                    return
                }
                val editableElement = currentMode.element
                if (editableElement !is ElementUI.Link) {
                    return
                }
                updateState {
                    copy(
                        mode = PageMode.Edit(
                            element = editableElement.copy(relatedPage = effect.page)
                        )
                    )
                }
                applyElementChanges()
            }
            else -> Unit
        }
    }

    private fun applyElementChanges() {
        when (val mode = state.value.mode) {
            is PageMode.Edit -> {
                val editableElement = mode.element
                editorUseCase.createOrUpdateElement(
                    pageId = state.value.pageId ?: return,
                    element = toElementApi(editableElement)
                ).onEach {
                    fetchPageData()
                    updateState {
                        copy(
                            mode = PageMode.Select,
                        )
                    }
                }.catch {
                    it.printStackTrace()
                }.launchIn(viewModelScope)
            }
            else -> return
        }
    }

    private fun onAddNewElementClick() {
        val elementToAdd = when (state.value.elementType) {
            ElementType.TEXT -> TextElement.createEmpty()
            ElementType.LINK -> LinkElement.createEmpty()
        }
        addNewElementToPage(elementToAdd)
    }

    private fun addNewElementToPage(element: Element) {
        editorUseCase.createOrUpdateElement(state.value.pageId ?: return, element)
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
        val fetchPageFlow = if (pageId != null) {
            editorUseCase.fetchPageById(pageId)
        } else {
            editorUseCase.fetchStartPage()
        }
        fetchPageFlow
            .onEach { page ->
                val elementsUI = fromElementsApi(page.elements)
                updateState {
                    copy(
                        pageId = page.id,
                        elements = elementsUI
                    )
                }
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
                offerEffect(NavigationEffect.Navigate(RootScreen.PickPageScreen))
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
                        relatedPage = element.relatedPage?.run {
                            PageUI(id = this.id, title = this.title)
                        },
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
                relatedPage = if (elementUi.relatedPage != null) {
                    Page(
                        id = elementUi.relatedPage.id,
                        title = elementUi.relatedPage.title,
                        elements = emptyList(),
                    )
                } else null,
            )
        }
    }

}

sealed class PageIntent : Intent {
    data class OnActionClick(val element: ElementUI, val action: ActionUI) : PageIntent()
    data class OnEditableElementChanged(val updatedElement: ElementUI) : PageIntent()
    data class OnElementClick(val element: ElementUI) : PageIntent()
    class OnReorderListElement(val oldPosition: Int, val newPosition: Int) : PageIntent()

    class OnStartDragging(val itemIndex: Int) : PageIntent()
    class OnSelectElementType(val elementType: ElementType) : PageIntent()

    object OnFinishDragging : PageIntent()

    object ToSampleScreen : PageIntent()
    object OnStartEditModeClick : PageIntent()
    object OnFinishEditModeClick : PageIntent()
    object OnApplyElementChangesClick : PageIntent()
    object OnDiscardChangesElementClick : PageIntent()
    object OnAddNewElementClick : PageIntent()
}

data class PageState(
    val pageId: String? = null,
    val textState: String = "Page UI state",
    val elements: List<ElementUI> = emptyList(),
    val mode: PageMode = PageMode.View,
    val draggableIndex: Int? = null,
    val elementType: ElementType = ElementType.TEXT,
) : State {
    val isDragging: Boolean get() = draggableIndex != null
}
