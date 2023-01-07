package com.pragmadreams.redaktor.entity

open class Element(
    open val id: String
)


data class TextElement(
    override val id: String,
    val text: String
) : Element(id)

data class LinkElement(
    override val id: String,
    val text: String,
    val relatedPageId: String?,
) : Element(id)