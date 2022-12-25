package com.pragmadreams.redaktor.android.presentation.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pragmadreams.redaktor.android.MyApplicationTheme
import com.pragmadreams.redaktor.android.base.ComposeView


class MainView : ComposeView<PageState, PageIntent>() {

    @Composable
    public override fun Content() {
        val vm: MainViewModel = viewModel()
        Content(state = vm.state.value, offerIntent = vm::handleIntent) {
            Column {
                Toolbar()
                ElementList()
            }
        }
    }

    @Composable
    private fun ElementList() {
        val paddingVert = 20.dp
        val paddHor = 100.dp

        val state = LocalState.current
        val offerIntent = LocalIntent.current

        LazyColumn {
            repeat(20) {
                item {
                    Surface(
                        modifier = Modifier
                            .padding(20.dp)
                            .border(1.dp, Color.Gray),
                    ) {
                        Text(
                            text = LocalState.current.textState,
                            modifier = Modifier.padding(
                                horizontal = paddHor,
                                vertical = paddingVert
                            )
                        )
                    }
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
            val offerIntent = LocalIntent.current

            Button(modifier = Modifier.padding(12.dp),
                onClick = {
                    offerIntent(PageIntent.SomeUserIntent)
                }) {
                Text(text = "Test")
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
//        CompositionLocalProvider(LocalState provides previewState) {
//            MyApplicationTheme {
//                Content()
//            }
//        }

//        MainView().Content(state = previewState, offerIntent = {}) {
//
//        }
    }

}