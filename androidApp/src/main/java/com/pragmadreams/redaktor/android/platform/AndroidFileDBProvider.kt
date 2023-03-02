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
              "pages": [
                {
                  "id": "5a060ed0-f681-4ab5-af9d-d5243ef9e0d9",
                  "title": "index",
                  "elements": []
                }
              ],
              "start_page_id": "5a060ed0-f681-4ab5-af9d-d5243ef9e0d9"
            }
        """.trimIndent()
    }

    override suspend fun saveJsonContent(content: String) {
        println("mylog Content of JsonDB: $content")
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