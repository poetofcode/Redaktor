package com.pragmadreams.redaktor.entity

open class Element(
    open var id: String,
) {
    var isNew: Boolean = false
}


data class TextElement(
    override var id: String,
    val text: String
) : Element(id) {
    companion object {
        fun createEmpty() = TextElement(
            id = String(),
            text = String(),
        ).apply { isNew = true }
    }
}

data class LinkElement(
    override var id: String,
    val text: String,
    val relatedPageId: String?,
) : Element(id)