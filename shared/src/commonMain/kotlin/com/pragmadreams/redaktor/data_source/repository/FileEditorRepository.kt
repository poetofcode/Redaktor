package com.pragmadreams.redaktor.data_source.repository

import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.LinkElement
import com.pragmadreams.redaktor.entity.Page
import com.pragmadreams.redaktor.entity.TextElement
import com.pragmadreams.redaktor.util.FileDBContentProvider
import com.pragmadreams.redaktor.util.createUUID
import com.pragmadreams.redaktor.util.swap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

class FileEditorRepository(
    val dbProvider: FileDBContentProvider,
) : EditorRepository {

    private var _data: PersistentData? = null
    private var dataOrDefault: PersistentData
        get() {
            if (_data != null) {
                return _data!!
            }
            return PersistentData.EMPTY
        }
        set(value) {
            _data = value
        }

    //
    // Сюда нужно добавить все классы-наследники Element
    //
    private val module = SerializersModule {
        polymorphic(Element::class, TextElement::class, TextElement.serializer())
        polymorphic(Element::class, LinkElement::class, LinkElement.serializer())
    }
    private val customJson = Json {
        serializersModule = module
        encodeDefaults = true
    }

    override suspend fun fetchStartPage(): Page {
        val startPageId = fetchStartPageId().takeIf { it.isNotBlank() } ?: createStartPage().id
        return fetchPageById(pageId = startPageId)
    }

    private suspend fun fetchStartPageId(): String {
        val data = fetchAllData()
        return data.startPageId
    }

    override suspend fun fetchPageById(pageId: String): Page {
        val data = fetchAllData()
        return data.pages.first { it.id == pageId }
    }

    override suspend fun fetchPages(): List<Page> {
        val data = fetchAllData()
        return data.pages.map {
            it.copy(
                elements = emptyList()
            )
        }
    }

    override suspend fun createOrUpdateElement(pageId: String, element: Element) {
        var foundPage = dataOrDefault.pages.firstOrNull {
            it.id == pageId
        } ?: throw Exception("Page with id{$pageId} not found")

        if (foundPage.elements.indexOfFirst { it.id == element.id } < 0) {
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
        saveAllData()
    }

    override suspend fun deleteElement(pageId: String, elementId: String) {
        // TODO

        saveAllData()
    }

    override suspend fun reorderElements(
        pageId: String,
        firstElementId: String,
        secondElementId: String
    ) {
        var foundPage = dataOrDefault.pages.firstOrNull {
            it.id == pageId
        } ?: throw Exception("Page with id{$pageId} not found")

        val firstIndex = foundPage.elements.first { it.id == firstElementId }
        val secondIndex = foundPage.elements.first { it.id == secondElementId }

        dataOrDefault = dataOrDefault.copy(pages = dataOrDefault.pages.map {
            if (it.id == pageId) {
                foundPage.copy(
                    elements = foundPage.elements.swap(
                        foundPage.elements.indexOf(firstIndex),
                        foundPage.elements.indexOf(secondIndex)
                    )
                )
            } else {
                it
            }
        })
        saveAllData()
    }

    private suspend fun createStartPage(): Page {
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
        saveAllData()
        return page
    }

    private suspend fun fetchAllData(): PersistentData {
        if (_data != null) {
            return _data!!
        }
        return try {
            val dbJson = dbProvider.provideJsonDB()
            customJson.decodeFromString<PersistentData>(dbJson).apply {
                _data = this
            }
        } catch (e: Throwable) {
            _data = PersistentData.EMPTY
            _data!!
        }
    }

    private suspend fun saveAllData() {
        val encoded = customJson.encodeToString(_data ?: return)
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