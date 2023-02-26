package com.pragmadreams.redaktor.util

interface TextFileDBContentProvider {

    suspend fun provideJsonDB() : String

    suspend fun saveJsonContent(content: String)

}

class EmptyDBContentProvider : TextFileDBContentProvider {
    override suspend fun provideJsonDB(): String = ""
    override suspend fun saveJsonContent(content: String) = Unit
}