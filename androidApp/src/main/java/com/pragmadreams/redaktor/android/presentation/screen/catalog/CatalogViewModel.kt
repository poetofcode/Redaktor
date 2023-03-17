package com.pragmadreams.redaktor.android.presentation.screen.catalog

import androidx.lifecycle.viewModelScope
import com.pragmadreams.redaktor.android.base.BaseViewModel
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.base.State
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
            .onEach {
                println("mylog $it")
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: CatalogIntent) {
        // TODO
    }

    override fun createState(): CatalogState = CatalogState()


}

data class CatalogState(
    val pages: List<Unit> = emptyList()
) : State

sealed class CatalogIntent : Intent