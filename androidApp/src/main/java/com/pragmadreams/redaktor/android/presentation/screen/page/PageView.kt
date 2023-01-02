package com.pragmadreams.redaktor.android.presentation.screen.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pragmadreams.redaktor.android.base.ComposeView


class PageView : ComposeView<PageState, PageIntent>() {

    @Composable
    override fun Layout() {
        Column {
            Toolbar()
            ElementList()
        }
    }

    @Composable
    private fun ElementList() {
        val paddingVert = 20.dp
        val paddHor = 16.dp

        val state = LocalState.current
        val offerIntent = LocalIntent.current

        LazyColumn {
            items(state.elements) {
                Text(
                    text = it.text,
                    modifier = Modifier.padding(
                        horizontal = paddHor,
                        vertical = paddingVert
                    )
                )
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
            val offerIntent = LocalIntent.current

            val buttonParams: Pair<String, () -> Unit> = when (state.mode) {
                PageMode.VIEW -> {
                    Pair("Ред.") { offerIntent(PageIntent.OnStartEditModeClick) }
                }
                PageMode.EDIT -> {
                    Pair("Просмотр") { offerIntent(PageIntent.OnFinishEditModeClick) }
                }
            }

            Button(modifier = Modifier.padding(horizontal = 12.dp),
                onClick = buttonParams.second) {
                Text(text = buttonParams.first)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Redaktor",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 20.dp),
                color = Color.Black
            )
        }
    }

    @Preview
    @Composable
    fun DefaultPreview() {
        val previewState = PageState(
            textState = "test state"
        )
        PageView().Content(previewState)
    }

}