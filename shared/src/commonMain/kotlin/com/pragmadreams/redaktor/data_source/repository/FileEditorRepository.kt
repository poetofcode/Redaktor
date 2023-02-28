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

    override suspend fun fetchStartPage(): Page {
        return fetchPageById(pageId = fetchStartPageId())
    }

    override suspend fun fetchPageById(pageId: String): Page {
        val data = fetchAllData()
        println("mylog data: $data")
        return data.pages.first { it.id == pageId }
    }

    override suspend fun fetchStartPageId(): String {
        val data = fetchAllData()
        return data.startPageId
    }

    override suspend fun createOrUpdateElement(pageId: String, element: Element) {
        println("mylog DB: $dataOrDefault, pageId: $pageId")

        var foundPage = dataOrDefault.pages.firstOrNull { it.id == pageId } ?: createNewPage()
        println("mylog Found element index: ${foundPage.elements.indexOfFirst { it.id == element.id }}")

        if (foundPage.elements.indexOfFirst { it.id == element.id } < 0) {
            println("mylog New elemtn ID: ${element.id}")

            foundPage = foundPage.copy(
                elements = foundPage.elements.toMutableList().apply {
                    add(element.run {
                        id = createUUID()
                        return@run this
                    })
                }.toList()
            )
        } else {
            foundPage = foundPage.copy(
                elements = foundPage.elements.map { item ->
                    if (item.id != element.id) {
                        item
                    } else {
                        element
                    }
                }
            )
        }
        dataOrDefault = dataOrDefault.copy(pages = dataOrDefault.pages.map {
            if (it.id == pageId) {
                foundPage
            } else {
                it
            }
        })

        println("mylog BEFORE_SAVE_DATA = $dataOrDefault, PAGE = $foundPage")
        saveAllData()
    }

    override suspend fun deleteElement(pageId: String, elementId: String) {
        // TODO

        saveAllData()
    }

    private suspend fun createNewPage(): Page {
        val pageId = createUUID()
        val page = Page(
            id = pageId,
            title = "index",
            elements = emptyList()
        )
        dataOrDefault = dataOrDefault.copy(
            pages = listOf(page),
            startPageId = pageId
        )
        println("mylog AFTER_CREATE_PAGE_DATA = $dataOrDefault")
        saveAllData()
        return page
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

    @SerialName("start_page_id")
    val startPageId: String,
) {
    companion object {
        val EMPTY = PersistentData(
            pages = emptyList(),
            startPageId = "",
        )
    }
}