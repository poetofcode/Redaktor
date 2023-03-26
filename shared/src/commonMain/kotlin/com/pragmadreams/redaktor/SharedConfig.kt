package com.pragmadreams.redaktor

import com.pragmadreams.redaktor.data_source.repository.FileEditorRepository
import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.util.EmptyDBContentProvider
import com.pragmadreams.redaktor.util.FileDBContentProvider

data class SharedConfig(
    val isInit: Boolean,
    val fileDBContentProvider: FileDBContentProvider
) {
    val editorRepository: EditorRepository = FileEditorRepository(fileDBContentProvider)

    companion object {
        fun setup(
            fileDBContentProvider: FileDBContentProvider
        ) {
            if (!INSTANCE.isInit) {
                INSTANCE = SharedConfig(
                    isInit = true,
                    fileDBContentProvider = fileDBContentProvider,
                )
            }
        }

        private val DEFAULT = SharedConfig(
            isInit = false,
            fileDBContentProvider = EmptyDBContentProvider()
        )

        var INSTANCE: SharedConfig = DEFAULT
            private set
    }
}

