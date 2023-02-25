package com.pragmadreams.redaktor.factory

import com.pragmadreams.redaktor.SharedConfig
import com.pragmadreams.redaktor.data_source.repository.FileEditorRepository
import com.pragmadreams.redaktor.domain.usecase.EditorUseCase

class FileUseCaseFactory : UseCaseFactory {
    override fun createEditorUseCase(): EditorUseCase {
        return EditorUseCase(
            repository = FileEditorRepository(
                dbProvider = SharedConfig.INSTANCE.fileDBContentProvider
            )
        )
    }
}