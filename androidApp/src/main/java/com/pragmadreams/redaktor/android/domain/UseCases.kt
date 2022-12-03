package com.pragmadreams.redaktor.android.domain

import com.pragmadreams.redaktor.domain.usecase.EditorUseCase
import com.pragmadreams.redaktor.factory.MockUseCaseFactory
import com.pragmadreams.redaktor.factory.UseCaseFactory

object UseCases {

    private val factory: UseCaseFactory = MockUseCaseFactory()

    val editorUseCase: EditorUseCase = factory.createEditorUseCase()

}