package com.pragmadreams.redaktor.util

interface TextFileDBContentProvider {

    suspend fun provideJsonDB() : String

}

class EmptyDBContentProvider : TextFileDBContentProvider {
    override suspend fun provideJsonDB(): String = ""
}