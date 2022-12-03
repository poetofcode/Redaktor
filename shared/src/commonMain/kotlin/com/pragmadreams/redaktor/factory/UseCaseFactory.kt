package com.pragmadreams.redaktor.factory

import com.pragmadreams.redaktor.domain.usecase.EditorUseCase

interface UseCaseFactory {

    fun createEditorUseCase() : EditorUseCase

}