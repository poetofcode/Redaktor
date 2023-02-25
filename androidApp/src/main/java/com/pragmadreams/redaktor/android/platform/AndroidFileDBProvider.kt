package com.pragmadreams.redaktor.android.platform

import com.pragmadreams.redaktor.util.TextFileDBContentProvider
import javax.inject.Inject

class AndroidFileDBProvider @Inject constructor() : TextFileDBContentProvider {
    override suspend fun provideJsonDB(): String {
        return """
            {
                "todo": 666
            }
        """.trimIndent()
    }
}