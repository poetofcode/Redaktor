package com.pragmadreams.redaktor.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class Element(
    open var id: String,
) {
    @SerialName("is_new")
    var isNew: Boolean = false
}

data class TextElement(
    override var id: String,

    @SerialName("text")
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

    @SerialName("text")
    val text: String,

    @SerialName("related_page_id")
    val relatedPageId: String?,
) : Element(id)