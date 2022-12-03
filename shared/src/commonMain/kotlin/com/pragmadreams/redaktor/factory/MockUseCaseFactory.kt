package com.pragmadreams.redaktor.factory

import com.pragmadreams.redaktor.data_source.repository.MockEditorRepository
import com.pragmadreams.redaktor.domain.usecase.EditorUseCase

class MockUseCaseFactory : UseCaseFactory {
    override fun createEditorUseCase(): EditorUseCase {
        return EditorUseCase(repository = MockEditorRepository())
    }
}