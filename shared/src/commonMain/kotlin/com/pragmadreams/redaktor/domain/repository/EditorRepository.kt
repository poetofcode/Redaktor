package com.pragmadreams.redaktor.domain.repository

import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.Page

interface EditorRepository {

    suspend fun fetchStartPage() : Page

    suspend fun fetchPageById(pageId: String) : Page

    // suspend fun fetchStartPageId() : String

    suspend fun fetchPages() : List<Page>

    suspend fun createOrUpdateElement(pageId: String, element: Element)

    suspend fun deleteElement(pageId: String, elementId: String)

    suspend fun reorderElements(pageId: String, firstElementId: String, secondElementId: String)

    suspend fun createPage()

    suspend fun updatePage(pageId: String, title: String)

}