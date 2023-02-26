package com.pragmadreams.redaktor.android.platform

import android.app.Application
import com.pragmadreams.redaktor.util.FileDBContentProvider
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AndroidFileDBProvider @Inject constructor(
    val application: Application,
) : FileDBContentProvider {
    override suspend fun provideJsonDB(): String {
        return """
            {
                "todo": 666
            }
        """.trimIndent()
    }

    override suspend fun saveJsonContent(content: String) {
        try {
            val cachePath = File(application.cacheDir, "db")
            cachePath.mkdirs()
            val stream = File("$cachePath/jsondb.dat")
            stream.printWriter().use {
                it.write(content)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // println("mylog Saving Json content: $content")
    }
}