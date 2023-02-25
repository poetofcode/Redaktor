package com.pragmadreams.redaktor

import com.pragmadreams.redaktor.util.EmptyDBContentProvider
import com.pragmadreams.redaktor.util.TextFileDBContentProvider

data class SharedConfig(
    val isInit: Boolean,
    val fileDBContentProvider: TextFileDBContentProvider
) {
    companion object {
        fun setup(
            fileDBContentProvider: TextFileDBContentProvider
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

