package com.pragmadreams.redaktor.entity

import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val id: String,
    val title: String,
    val elements: List<Element>,
)