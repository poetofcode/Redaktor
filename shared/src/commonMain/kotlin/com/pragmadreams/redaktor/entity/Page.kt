package com.pragmadreams.redaktor.entity

data class Page(
    val id: String,
    val title: String,
    val elements: List<Element>,
)