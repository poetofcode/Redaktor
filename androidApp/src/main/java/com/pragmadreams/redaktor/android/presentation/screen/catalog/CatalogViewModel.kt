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
                // TODO
            }
        }
    }

    override fun createState(): CatalogState = CatalogState()

}

data class CatalogState(
    val pages: List<PageUI> = emptyList()
) : State

sealed class CatalogIntent : Intent {
    object OnAddPageClick : CatalogIntent()

    class OnPageClick(val pageId: String) : CatalogIntent()
}