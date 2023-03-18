package com.pragmadreams.redaktor.android.presentation.screen.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pragmadreams.redaktor.android.base.ComposeView
import com.pragmadreams.redaktor.android.domain.model.PageMode
import com.pragmadreams.redaktor.android.presentation.screen.page.PageIntent

class CatalogView : ComposeView<CatalogState, CatalogIntent>() {

    @Composable
    override fun Layout() {
        Column {
            Toolbar()
            PageList()
        }
    }

    @Composable
    private fun PageList() {
        val state = LocalState.current
        val offerIntent = LocalOfferIntent.current
        LazyColumn {
            items(state.pages) { page ->
                Column(Modifier.clickable {
                    offerIntent(CatalogIntent.OnPageClick(pageId = page.id))
                }) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = page.title,
                            modifier = Modifier
                                .padding(),
                            color = Color.Black
                        )
                    }
                    Divider(Modifier.fillMaxSize())
                }
            }
        }
    }

    @Composable
    private fun Toolbar() {
        Row(
            Modifier
                .background(color = Color.Green)
                .height(54.dp)
                .fillMaxWidth()
        ) {
            val state = LocalState.current
            val offerIntent = LocalOfferIntent.current

            Text(
                text = "Redaktor",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 20.dp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                onClick = { offerIntent(CatalogIntent.OnAddPageClick) }
            ) {
                Icon(
                    modifier = Modifier.padding(2.dp),
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
    }


}