package com.pragmadreams.redaktor.data_source.repository

import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.Page
import com.pragmadreams.redaktor.util.TextFileDBContentProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FileEditorRepository(
    val dbProvider: TextFileDBContentProvider,
) : EditorRepository {

    private var _data: PersistantData? = null

    private suspend fun fetchAllData() : PersistantData {
        if (_data != null) {
            return _data!!
        }
        val dbJson = dbProvider.provideJsonDB()
        return Json.decodeFromString<PersistantData>(dbJson).apply {
            _data = this
        }
    }

    override suspend fun fetchPageById(pageId: String): Page {
        val data = fetchAllData()
        println("mylog data: $data")
        TODO("Not yet implemented")
    }

    override suspend fun createOrUpdateElement(pageId: String, element: Element) {
    }

    override suspend fun deleteElement(pageId: String, elementId: String) {
        TODO("Not yet implemented")
    }
}

@Serializable
data class PersistantData(
    @SerialName("todo") val todo: Int
)