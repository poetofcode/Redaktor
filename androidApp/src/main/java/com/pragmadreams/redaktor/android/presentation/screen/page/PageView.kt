package com.pragmadreams.redaktor.android.presentation.screen.page

import androidx.compose.animation.expandIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pragmadreams.redaktor.android.base.ComposeView


class PageView : ComposeView<PageState, PageIntent>() {

    @Composable
    override fun Layout() {
        Column {
            Toolbar()
            Box(Modifier.fillMaxSize()) {
                val floatingToolbarHeight = 50.dp
                ElementList(contentPaddingBottom = floatingToolbarHeight)
                FloatingToolbar(
                    Modifier
                        .height(floatingToolbarHeight)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }

    @Composable
    private fun ElementList(contentPaddingBottom: Dp) {
        val state = LocalState.current
        LazyColumn(contentPadding = PaddingValues(bottom = contentPaddingBottom)) {
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
        val state = LocalState.current
        val editableElement = state.editableElement
        Column {
            when (element) {
                is ElementUI.Text -> {
                    if (editableElement is ElementUI.Text && editableElement.id == element.id) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = editableElement.text,
                            onValueChange = {
                                offerIntent(
                                    PageIntent.OnEditableElementChanged(editableElement.copy(text = it))
                                )
                            })
                    } else {
                        Text(
                            text = element.text,
                            modifier = Modifier.padding(
                                horizontal = paddHor,
                                vertical = paddingVert
                            )
                        )
                    }

                    // Actions with divider
                    when (LocalState.current.mode) {
                        PageMode.SELECT -> {
                            Divider(
                                thickness = 1.dp,
                                color = Color.LightGray,
                                modifier = Modifier.padding(horizontal = paddHor)
                            )

                            Row(
                                modifier = Modifier.padding(horizontal = paddHor),
                            ) {
                                Spacer(Modifier.weight(1f))
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
        ActionButton(
            imageVector = when (action) {
                ActionUI.Delete -> Icons.Filled.Delete
                ActionUI.Edit -> Icons.Filled.Edit
            }
        ) {
            offerIntent(PageIntent.OnActionClick(element, action))
        }
    }

    @Composable
    private fun ActionButton(
        modifier: Modifier = Modifier,
        imageVector: ImageVector,
        onClick: () -> Unit
    ) {
        Box(modifier = modifier
            .clickable { onClick() }
            .border(width = 1.dp, color = Color.LightGray)
            .padding(5.dp)) {
            Icon(
                imageVector = imageVector,
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
                PageMode.SELECT -> {
                    Pair("Просмотр") { offerIntent(PageIntent.OnFinishEditModeClick) }
                }
                PageMode.EDIT -> {
                    Pair("Сохранить") { offerIntent(PageIntent.OnApplyElementChangesClick) }
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

    @Composable
    private fun FloatingToolbar(modifier: Modifier) {
        val state = LocalState.current
        val offerIntent = LocalOfferIntent.current
        when (state.mode) {
            PageMode.EDIT -> {
                Row(
                    modifier
                        .fillMaxWidth()
                        .background(Color.Cyan)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ActionButton(imageVector = Icons.Filled.Cancel) {
                        offerIntent(PageIntent.OnDiscardChangesElementClick)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    ActionButton(imageVector = Icons.Filled.Done) {
                        offerIntent(PageIntent.OnApplyElementChangesClick)
                    }
                }
            }
            else -> Unit
        }
    }

    @Preview
    @Composable
    fun DefaultPreview() {
        val previewState = PageState(
            textState = "test state",
            elements = emptyList(),
            mode = PageMode.EDIT,
        )
        PageView().Content(previewState)
    }

}