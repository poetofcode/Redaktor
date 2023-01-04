package com.pragmadreams.redaktor.data_source.repository

import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.entity.Element
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
            TextElement(
                id = "5",
                text = "Минималистичным интерфейсом (идея = больше контента - лучше)"
            ),
        )
    )

    override suspend fun fetchPageById(pageId: String): Page {
        return testPage
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