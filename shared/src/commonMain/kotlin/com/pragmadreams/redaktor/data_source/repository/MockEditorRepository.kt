package com.pragmadreams.redaktor.data_source.repository

import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.Page
import com.pragmadreams.redaktor.entity.TextElement

internal class MockEditorRepository : EditorRepository {
    private val testPage = Page(
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
        TODO("Not yet implemented")
    }

    override suspend fun deleteElement(pageId: String, elementId: String) {
        TODO("Not yet implemented")
    }

}