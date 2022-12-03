package com.pragmadreams.redaktor.domain.usecase

import com.pragmadreams.redaktor.domain.repository.EditorRepository
import com.pragmadreams.redaktor.entity.Element
import com.pragmadreams.redaktor.entity.Page
import com.pragmadreams.redaktor.util.CommonFlow
import com.pragmadreams.redaktor.util.flowOnDefaultContext
import kotlinx.coroutines.flow.flow

class EditorUseCase constructor(
    private val repository: EditorRepository
) {
    fun fetchPageById(pageId: String): CommonFlow<Page> = flow<Page> {
        repository.fetchPageById(pageId)
    }.flowOnDefaultContext()


    fun createOrUpdateElement(pageId: String, element: Element): CommonFlow<Unit> = flow<Unit> {
        repository.createOrUpdateElement(pageId, element)
    }.flowOnDefaultContext()

    fun deleteElement(pageId: String, elementId: String): CommonFlow<Unit> = flow<Unit> {
        repository.deleteElement(pageId, elementId)
    }.flowOnDefaultContext()
}