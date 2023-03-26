package com.pragmadreams.redaktor.factory

import com.pragmadreams.redaktor.SharedConfig
import com.pragmadreams.redaktor.domain.usecase.EditorUseCase

class FileUseCaseFactory : UseCaseFactory {
    override fun createEditorUseCase(): EditorUseCase {
        return EditorUseCase(SharedConfig.INSTANCE.editorRepository)
    }
}