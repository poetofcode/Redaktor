package com.pragmadreams.redaktor.android.platform

import android.app.Application
import com.pragmadreams.redaktor.util.FileDBContentProvider
import java.io.File
import javax.inject.Inject

class AndroidFileDBProvider @Inject constructor(
    val application: Application,
) : FileDBContentProvider {
    override suspend fun provideJsonDB(): String {
        val cachePath = File(application.cacheDir, "db")
        cachePath.mkdirs()
        val stream = File("$cachePath/jsondb.dat").bufferedReader()
        return stream.use { it.readText() }
    }

    override suspend fun saveJsonContent(content: String) {
        val cachePath = File(application.cacheDir, "db")
        cachePath.mkdirs()
        val stream = File("$cachePath/jsondb.dat")
        stream.printWriter().use {
            it.write(content)
        }
    }

}