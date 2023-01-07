package com.pragmadreams.redaktor.data_source.repository

import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.LinkElement
import com.pragmadreams.redaktor.entity.Page
import com.pragmadreams.redaktor.entity.TextElement

internal class MockEditorRepository : EditorRepository {
    private var testPage = Page(
        id = "1",
        title = "Test page",
        elements = listOf(
            TextElement(
                id = "2",
                text = "Redaktor - блокнот с возможностями:"
            ),
            TextElement(
                id = "3",
                text = "Создание иерархических ссылок"
            ),
            TextElement(
                id = "4",
                text = "Возможности работать без сети/локально"
            ),
            LinkElement(
                id = "6",
                text = "Открыть page2",
                relatedPageId = "2",
            ),
            TextElement(
                id = "5",
                text = "Минималистичным интерфейсом (идея = больше контента - лучше)"
            ),
        )
    )
    private var testPage2 = Page(
        id = "2",
        title = "Test page 2",
        elements = listOf(
            TextElement(
                id = "21",
                text = "Заголовок страницы 2"
            ),
            LinkElement(
                id = "22",
                text = "Открыть page1",
                relatedPageId = "1",
            ),
        )
    )
    val pages = mutableListOf(testPage, testPage2)

    override suspend fun fetchPageById(pageId: String): Page {
        return pages.first { it.id == pageId }
    }

    override suspend fun createOrUpdateElement(pageId: String, element: Element) {
        if (testPage.elements.indexOfFirst { it.id == element.id } < 0) {
            testPage = testPage.copy(
                elements = testPage.elements.toMutableList().apply { add(element) }.toList()
            )
            return
        }
        testPage = testPage.copy(
            elements = testPage.elements.map { item ->
                if (item.id != element.id) {
                    item
                } else {
                    element
                }
            }
        )
    }

    override suspend fun deleteElement(pageId: String, elementId: String) {
        TODO("Not yet implemented")

    }

}