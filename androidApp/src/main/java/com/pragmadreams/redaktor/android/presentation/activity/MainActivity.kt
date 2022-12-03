package com.pragmadreams.redaktor.android.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pragmadreams.redaktor.android.MyApplicationTheme

class MainActivity : ComponentActivity() {
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
}

@Composable
fun ContentView(vm: MainViewModel = viewModel()) {
    Text(text = vm.textState.value)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        ContentView()
    }
}

