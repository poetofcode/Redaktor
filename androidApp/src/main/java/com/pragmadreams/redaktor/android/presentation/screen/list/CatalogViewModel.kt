package com.pragmadreams.redaktor.android.presentation.screen.list

import com.pragmadreams.redaktor.android.base.BaseViewModel
import com.pragmadreams.redaktor.android.base.Intent
import com.pragmadreams.redaktor.android.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(

) : BaseViewModel<CatalogState, CatalogIntent>() {

    override fun handleIntent(intent: CatalogIntent) {
        // TODO
    }

    override fun createState(): CatalogState = CatalogState()


}

data class CatalogState(
    val pages: List<Unit> = emptyList()
) : State

sealed class CatalogIntent: Intent