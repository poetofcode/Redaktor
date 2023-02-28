package com.pragmadreams.redaktor.data_source.repository

import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.Page
import com.pragmadreams.redaktor.util.FileDBContentProvider
import com.pragmadreams.redaktor.util.createUUID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FileEditorRepository(
    val dbProvider: FileDBContentProvider,
) : EditorRepository {

    private var _data: PersistentData? = null
    private var dataOrDefault: PersistentData
        get() {
            if (_data != null) return _data!!
            return PersistentData.EMPTY
        }
        set(value) { _data = value }

    override suspend fun fetchPageById(pageId: String): Page {
        val data = fetchAllData()
        println("mylog data: $data")
        TODO("Not yet implemented")
    }

    override suspend fun createOrUpdateElement(pageId: String, element: Element) {
        val data = dataOrDefault
        println("mylog DB: $dataOrDefault, pageId: $pageId")

        val pages = data.pages
        var found = pages.firstOrNull { it.id == pageId } ?: createNewPage()
        println("mylog Found element index: ${found.elements.indexOfFirst { it.id == element.id }}")

        if (found.elements.indexOfFirst { it.id == element.id } < 0) {
            println("mylog New elemtn ID: ${element.id}")

            found = found.copy(
                elements = found.elements.toMutableList().apply {
                    add(element.run {
                        id = createUUID()
                        return@run this
                    })
                }.toList()
            )
        } else {
            found = found.copy(
                elements = found.elements.map { item ->
                    if (item.id != element.id) {
                        item
                    } else {
                        element
                    }
                }
            )
        }
        dataOrDefault = dataOrDefault.copy(pages = pages.map {
            if (it.id == pageId) {
                found
            } else {
                it
            }
        })
        saveAllData()
    }

    private fun createNewPage(): Page {
        TODO()
    }

    override suspend fun deleteElement(pageId: String, elementId: String) {
        // TODO

        saveAllData()
    }

    private suspend fun fetchAllData(): PersistentData {
        if (_data != null) {
            return _data!!
        }
        val dbJson = dbProvider.provideJsonDB()
        return Json.decodeFromString<PersistentData>(dbJson).apply {
            _data = this
        }
    }

    private suspend fun saveAllData() {
        val encoded = Json.encodeToString(_data ?: return)
        dbProvider.saveJsonContent(encoded)
    }
}

@Serializable
data class PersistentData(

    @SerialName("pages")
    val pages: List<Page>,
) {
    companion object {
        val EMPTY = PersistentData(
            pages = emptyList()
        )
    }
}