package com.pragmadreams.redaktor.android.presentation.screen.page

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
        val state = LocalState.current
        LazyColumn {
            items(state.elements) {
                ElementItem(it)
            }
        }
    }

    @Composable
    private fun ElementItem(element: ElementUI) {
        val paddingVert = 20.dp
        val paddHor = 16.dp
        val offerIntent = LocalOfferIntent.current
        Column {
            when (element) {
                is ElementUI.Text -> {
                    Text(
                        text = element.text,
                        modifier = Modifier.padding(
                            horizontal = paddHor,
                            vertical = paddingVert
                        )
                    )

                    // Actions with divider
                    when (LocalState.current.mode) {
                        PageMode.EDIT -> {
                            Divider(
                                thickness = 1.dp,
                                color = Color.LightGray,
                                modifier = Modifier.padding(horizontal = paddHor)
                            )

                            Row(
                                modifier = Modifier.padding(horizontal = paddHor),
                                horizontalArrangement = Arrangement.End
                            ) {
                                element.actions.forEach {
                                    ActionItem(action = it, element = element)
                                }
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    @Composable
    private fun ActionItem(action: ActionUI, element: ElementUI) {
        val offerIntent = LocalOfferIntent.current
        Box(modifier = Modifier.clickable {
            offerIntent(PageIntent.OnActionClick(element))
        }) {
            Icon(
                imageVector = when (action) {
                    ActionUI.Delete -> Icons.Filled.Delete
                    ActionUI.Edit -> Icons.Filled.Edit
                },
                contentDescription = null
            )
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

            val buttonParams: Pair<String, () -> Unit> = when (state.mode) {
                PageMode.VIEW -> {
                    Pair("Ред.") { offerIntent(PageIntent.OnStartEditModeClick) }
                }
                PageMode.EDIT -> {
                    Pair("Просмотр") { offerIntent(PageIntent.OnFinishEditModeClick) }
                }
            }

            Button(
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = buttonParams.second
            ) {
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