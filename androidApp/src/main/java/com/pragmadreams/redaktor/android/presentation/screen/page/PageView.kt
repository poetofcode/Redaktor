package com.pragmadreams.redaktor.android.presentation.screen.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pragmadreams.redaktor.android.base.ComposeView
import com.pragmadreams.redaktor.android.domain.model.ActionUI
import com.pragmadreams.redaktor.android.domain.model.ElementUI
import com.pragmadreams.redaktor.android.domain.model.PageMode
import com.pragmadreams.redaktor.android.util.compose.drag_and_drop_list.DragDropList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PageView : ComposeView<PageState, PageIntent>() {

    @Composable
    override fun Layout() {
        val state = LocalState.current
        val offerIntent = LocalOfferIntent.current
        BackHandler(enabled = state.mode != PageMode.View) {
            when (state.mode) {
                is PageMode.Edit -> offerIntent(PageIntent.OnDiscardChangesElementClick)
                PageMode.Select -> offerIntent(PageIntent.OnFinishEditModeClick)
                else -> Unit
            }
        }

        val focusRequester = remember { FocusRequester() }

        // TODO Запилить что-то вроде CollectEffects и в нём обозревать эффект на старт редактирования
        //      текстового элемента: OnTextElementStartEditingEffect
        //      по нему делать: focusRequester.requestFocus()
        //      https://stackoverflow.com/questions/64181930/request-focus-on-textfield-in-jetpack-compose

        //      TODO АКТУАЛЬНО ЛИ ДАННОЕ TO_DO ???

        val coroutineScope = rememberCoroutineScope()
        val modalSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
            skipHalfExpanded = true,
        )

        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            sheetContent = {
                ModalBottomSheetContent(coroutineScope, modalSheetState)
            }
        ) {
            PageContent(focusRequester, onOptionButtonClick = {
                coroutineScope.launch { modalSheetState.show() }
            })
        }
    }

    @Composable
    private fun ModalBottomSheetContent(
        coroutineScope: CoroutineScope,
        modalSheetState: ModalBottomSheetState
    ) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.size(20.dp))

            ElementTypeItem("Текстовый элемент", {})

            Button(
                modifier = Modifier.padding(bottom = 20.dp),
                onClick = {
                    coroutineScope.launch { modalSheetState.hide() }
                }
            ) {
                Text(text = "Закрыть")
            }
        }
    }

    @Composable
    private fun ElementTypeItem(title: String, onClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
                .clickable { onClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
        {
            Text(
                text = title,
                modifier = Modifier.padding()
            )
        }
    }

    @Composable
    private fun PageContent(focusRequester: FocusRequester, onOptionButtonClick: () -> Unit) {
        Column {
            Toolbar()
            Box(Modifier.fillMaxSize()) {
                val floatingToolbarHeight = 50.dp
                ElementList(
                    contentPaddingBottom = floatingToolbarHeight,
                    focusRequester = focusRequester
                )
                FloatingToolbar(
                    Modifier
                        .height(floatingToolbarHeight)
                        .align(Alignment.BottomCenter)
                )
                AddElementButton(
                    modifier = Modifier
                        .height(floatingToolbarHeight)
                        .align(Alignment.BottomCenter),
                    onOptionButtonClick = onOptionButtonClick
                )
            }
        }
    }

    @Composable
    private fun AddElementButton(
        modifier: Modifier = Modifier,
        onOptionButtonClick: () -> Unit,
    ) {
        val state = LocalState.current
        val offerIntent = LocalOfferIntent.current
        when (state.mode) {
            is PageMode.Select -> {
                Row(modifier.fillMaxWidth()) {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(Color.Cyan)
                        .clickable { offerIntent(PageIntent.OnAddNewElementClick) }
                        .padding(5.dp)
                    ) {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            imageVector = Icons.Filled.Add,
                            contentDescription = null
                        )
                    }
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .width(50.dp)
                        .background(Color.Green)
                        .clickable {
                            onOptionButtonClick()
                        }
                        .padding(5.dp)
                    ) {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            imageVector = Icons.Filled.ArrowDropUp,
                            contentDescription = null
                        )
                    }
                }
            }
            else -> Unit
        }
    }

    @Composable
    private fun ElementList(contentPaddingBottom: Dp, focusRequester: FocusRequester) {
        val state = LocalState.current
        val offerIntent = LocalOfferIntent.current

        DragDropList(
            items = state.elements,
            itemView = {
                ElementItem(it, focusRequester)
            },
            contentPadding = PaddingValues(bottom = contentPaddingBottom),
            onMove = { oldPos, newPos ->
                offerIntent(PageIntent.OnReorderListElement(oldPosition = oldPos, newPosition = newPos))
            },
            onStartDragging = { itemIndex ->
                offerIntent(PageIntent.OnStartDragging(itemIndex ?: return@DragDropList))
            },
            onStopDragging = {
                offerIntent(PageIntent.OnFinishDragging)
            },
            isDraggable = state.mode is PageMode.Select
        )
    }

    @Composable
    private fun ElementItem(element: ElementUI, focusRequester: FocusRequester) {
        val paddingVert = 20.dp
        val paddHor = 16.dp
        val offerIntent = LocalOfferIntent.current
        val state = LocalState.current
        val editableElement: ElementUI? = (state.mode as? PageMode.Edit)?.element
        Column(
            modifier = Modifier
                .then(if (state.isDragging) {
                    Modifier
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                        .then(if (element.id == state.elements[state.draggableIndex!!].id) {
                            Modifier.background(Color.Yellow)
                        } else Modifier)
                } else Modifier)
        ) {
            when (element) {
                is ElementUI.Text -> {
                    if (editableElement is ElementUI.Text && editableElement.id == element.id) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .focusRequester(focusRequester),
                            value = editableElement.text,
                            onValueChange = {
                                offerIntent(
                                    PageIntent.OnEditableElementChanged(editableElement.copy(text = it))
                                )
                            })
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        Text(
                            text = element.text,
                            modifier = Modifier.padding(
                                horizontal = paddHor,
                                vertical = paddingVert
                            )
                        )
                    }
                }
                is ElementUI.Link -> {
                    if (editableElement is ElementUI.Link && editableElement.id == element.id) {
                        Column(Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                value = editableElement.text,
                                onValueChange = {
                                    offerIntent(
                                        PageIntent.OnEditableElementChanged(
                                            editableElement.copy(text = it)
                                        )
                                    )
                                })

                            Row(Modifier.padding(10.dp)) {
                                val color = if (element.isBound) Color(0, 200, 0, 255) else Color.Gray
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    imageVector = if (element.isBound) Icons.Filled.Link else Icons.Filled.LinkOff,
                                    contentDescription = null,
                                    tint = color,
                                )
                                Text(
                                    text = if (element.isBound) {
                                        "Ссылка установлена"
                                    } else {
                                        "Ссылка не задана"
                                    },
                                    modifier = Modifier.padding(start = 10.dp),
                                    fontStyle = FontStyle.Italic,
                                    color = color,
                                )
                            }
                        }
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        Row(
                            Modifier
                                .clickable { offerIntent(PageIntent.OnElementClick(element)) }
                                .padding(
                                    horizontal = paddHor,
                                    vertical = paddingVert
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = element.text,
                                modifier = Modifier.weight(1f),
                            )
                            Icon(
                                modifier = Modifier.padding(start = 10.dp),
                                imageVector = if (element.isBound) Icons.Filled.ArrowForward else Icons.Filled.LinkOff,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            // Actions with divider
            when (LocalState.current.mode) {
                PageMode.Select -> {
                    Divider(
                        thickness = 1.dp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = paddHor)
                    )

                    Row(
                        modifier = Modifier.padding(horizontal = paddHor),
                    ) {
                        Spacer(Modifier.weight(1f))
                        listOf(ActionUI.Edit, ActionUI.Delete).forEach {
                            ActionItem(action = it, element = element)
                        }
                    }
                }
                else -> Unit
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
                ActionUI.BindLink -> Icons.Filled.Link
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
                PageMode.View -> {
                    Pair("Ред.") { offerIntent(PageIntent.OnStartEditModeClick) }
                }
                PageMode.Select -> {
                    Pair("Просмотр") { offerIntent(PageIntent.OnFinishEditModeClick) }
                }
                is PageMode.Edit -> {
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
            is PageMode.Edit -> {
                val element = state.mode.element
                val actions = state.mode.element.actions
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
                    // Row of dynamic actions
                    actions.forEach {
                        ActionItem(action = it, element = element)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
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
            mode = PageMode.View,
        )
        PageView().Content(previewState)
    }

}