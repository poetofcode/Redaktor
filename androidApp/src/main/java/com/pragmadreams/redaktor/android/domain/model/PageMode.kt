package com.pragmadreams.redaktor.android.domain.model

sealed class PageMode {
    object View : PageMode()
    object Select : PageMode()
    data class Edit(val element: ElementUI) : PageMode()
}