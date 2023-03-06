package com.pragmadreams.redaktor.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page(
    @SerialName("id")
    val id: String,

    @SerialName("title")
    val title: String,

    @SerialName("elements")
    val elements: List<Element>,
)