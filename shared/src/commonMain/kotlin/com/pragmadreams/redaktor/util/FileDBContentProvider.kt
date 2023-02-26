package com.pragmadreams.redaktor.util

interface FileDBContentProvider {

    suspend fun provideJsonDB() : String

    suspend fun saveJsonContent(content: String)

}

class EmptyDBContentProvider : FileDBContentProvider {
    override suspend fun provideJsonDB(): String = ""
    override suspend fun saveJsonContent(content: String) = Unit
}