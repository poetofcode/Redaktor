package com.pragmadreams.redaktor.domain.usecase

import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.Page
import com.pragmadreams.redaktor.util.CommonFlow
import com.pragmadreams.redaktor.util.flowOnDefaultContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EditorUseCase constructor(
    private val repository: EditorRepository
) {
    fun fetchPages() : CommonFlow<List<Page>> = flow {
        emit(repository.fetchPages())
    }.flowOnDefaultContext()

    fun createPage() : CommonFlow<Unit> = flow {
        repository.createPage()
        emit(Unit)
    }.flowOnDefaultContext()

    fun updatePage(pageId: String, title: String) : CommonFlow<Unit> = flow {

        // TODO call repository


        emit(Unit)
    }.flowOnDefaultContext()

    fun fetchPageById(pageId: String): CommonFlow<Page> = flow {
        emit(repository.fetchPageById(pageId))
    }.flowOnDefaultContext()

    fun fetchStartPage() : CommonFlow<Page> = flow {
        emit(repository.fetchStartPage())
    }.flowOnDefaultContext()

    fun createOrUpdateElement(pageId: String, element: Element): CommonFlow<Unit> = flow {
        repository.createOrUpdateElement(pageId, element)
        emit(Unit)
    }.flowOnDefaultContext()

    fun reorderElements(pageId: String, firstElementId: String, secondElementId: String): CommonFlow<Unit> = flow {
        repository.reorderElements(pageId, firstElementId, secondElementId)
        emit(Unit)
    }.flowOnDefaultContext()

    fun deleteElement(pageId: String, elementId: String): CommonFlow<Unit> = flow {
        repository.deleteElement(pageId, elementId)
        emit(Unit)
    }.flowOnDefaultContext()
}