package com.pragmadreams.redaktor.android.presentation.screen.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.pragmadreams.redaktor.android.base.ComposeView

class CatalogView : ComposeView<CatalogState, CatalogIntent>() {

    @Composable
    override fun Layout() {
        Column {
            Text(text = "Page 1")
            Text(text = "Page 2")
        }
    }

}