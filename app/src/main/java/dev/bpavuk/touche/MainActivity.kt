package dev.bpavuk.touche

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dev.bpavuk.touche.ui.theme.ToucheTheme

const val EXTRA_FORCE_ONBOARDING = "dev.bpavuk.touche.extra.FORCE_ONBOARDING"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val forceOnboarding = intent.getBooleanExtra(
            EXTRA_FORCE_ONBOARDING,
            false
        )

        setContent {
            ToucheTheme {
                AppRoot(
                    forceOnboarding = forceOnboarding,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
