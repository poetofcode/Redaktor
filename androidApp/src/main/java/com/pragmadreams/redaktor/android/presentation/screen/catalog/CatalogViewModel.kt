package com.pragmadreams.redaktor.android.presentation.screen.catalog

import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.base.BaseViewModel
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.base.State
import com.pragmadreams.redaktor.android.domain.model.PageUI
import com.pragmadreams.redaktor.android.navigation.NavigationEffect
import com.pragmadreams.redaktor.android.navigation.RootScreen
import com.pragmadreams.redaktor.domain.usecase.EditorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val editorUseCase: EditorUseCase,
) : BaseViewModel<CatalogState, CatalogIntent>() {

    init {
        fetchData()
    }

    private fun fetchData() {
        editorUseCase.fetchPages()
            .onEach { pages ->
                updateState {
                    copy(
                        pages = pages.map { page ->
                            PageUI(
                                id = page.id,
                                title = page.title,
                            )
                        }
                    )
                }
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.OnPageClick -> {
                offerEffect(
                    effect = NavigationEffect.Navigate(
                        RootScreen.PageScreen.withArguments("pageId" to intent.pageId)
                    )
                )
            }

            CatalogIntent.OnAddPageClick -> {
                addNewPage()
            }
            is CatalogIntent.OnDeleteClick -> {
                // TODO implememnt
            }
            is CatalogIntent.OnEditClick -> {
                val selectedPage = state.value.pages.first { it.id == intent.pageId }
                updateState {
                    copy(
                        editablePage = selectedPage
                    )
                }
            }
            CatalogIntent.OnApplyEditClick -> {
                applyChanges()
            }
            CatalogIntent.OnCancelEditClick -> {
                updateState { copy(editablePage = null) }
            }
            is CatalogIntent.OnEditablePageChanged -> {
                updateState { copy(editablePage = intent.newPage) }
            }
        }
    }

    private fun applyChanges() {
        val updatedPage = state.value.editablePage?.copy() ?: return
        editorUseCase.updatePage(pageId = updatedPage.id, title = updatedPage.title)
            .onEach {
                updateState {
                    copy(
                        editablePage = null,
                        pages = state.value.pages.map {
                            if (it.id == updatedPage.id) {
                                updatedPage
                            } else {
                                it
                            }
                        }
                    )
                }
            }
            .catch { it.printStackTrace() }
            .launchIn(viewModelScope)
    }

    private fun addNewPage() {
        editorUseCase.createPage()
            .onEach { fetchData() }
            .catch { it.printStackTrace() }
            .launchIn(viewModelScope)
    }

    override fun createState(): CatalogState = CatalogState()

}

data class CatalogState(
    val pages: List<PageUI> = emptyList(),
    val editablePage: PageUI? = null,
) : State {
    val isEditing: Boolean get() = editablePage != null
}

sealed class CatalogIntent : Intent {
    object OnAddPageClick : CatalogIntent()
    object OnApplyEditClick : CatalogIntent()

    object OnCancelEditClick : CatalogIntent()

    class OnPageClick(val pageId: String) : CatalogIntent()
    class OnEditClick(val pageId: String) : CatalogIntent()

    class OnDeleteClick(val pageId: String) : CatalogIntent()
    class OnEditablePageChanged(val newPage: PageUI) : CatalogIntent()
}