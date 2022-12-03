package com.pragmadreams.redaktor.entity

open class Element(
    id: String
)


data class TextElement(
    val id: String,
    val text: String
) : Element(id)