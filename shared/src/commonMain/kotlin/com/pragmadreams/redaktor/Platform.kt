package com.pragmadreams.redaktor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform