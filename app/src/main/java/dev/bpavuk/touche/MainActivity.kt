package dev.bpavuk.touche

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dev.bpavuk.touche.input.DummyInputHandler
import dev.bpavuk.touche.input.InputViewModel
import dev.bpavuk.touche.input.StylusSurface
import dev.bpavuk.touche.ui.theme.ToucheTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val inputViewModel = InputViewModel(DummyInputHandler())

        setContent {
            ToucheTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StylusSurface(
                        inputViewModel,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
            }
        }
    }
}
