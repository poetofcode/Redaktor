package com.pragmadreams.redaktor.android.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pragmadreams.redaktor.android.MyApplicationTheme

class MainActivity : ComponentActivity() {

    val emptyLambda: (PageUiIntent) -> Unit = {}

    val LocalIntent = staticCompositionLocalOf { emptyLambda }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // TODO remove Greeting class

                    ContentView()
                }
            }
        }
    }

    @Composable
    fun ContentView(vm: MainViewModel = viewModel()) {
        val paddingVert = 20.dp
        val paddHor = 100.dp
        CompositionLocalProvider(LocalIntent provides vm::handleUiIntent) {
            Column {
                Toolbar()

                LazyColumn {
                    repeat(20) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .border(1.dp, Color.Gray)
                                    .clickable { vm.handleUiIntent(PageUiIntent.SomeUserIntent) },
                            ) {
                                Text(
                                    text = vm.textState.value,
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
        }

    }


    @Composable
    fun Toolbar() {
        Row(
            Modifier
                .background(color = Color.Green)
                .height(54.dp)
                .fillMaxWidth()
        ) {
            val offerIntent = LocalIntent.current

            Button(modifier = Modifier.padding(12.dp),
                onClick = {
                    offerIntent(PageUiIntent.SomeUserIntent)
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
        MyApplicationTheme {
            ContentView()
        }
    }

}

