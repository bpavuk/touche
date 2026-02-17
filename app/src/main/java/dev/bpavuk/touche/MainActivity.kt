package dev.bpavuk.touche

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dev.bpavuk.touche.data.persistence.OnboardingRepository
import dev.bpavuk.touche.ui.theme.ToucheTheme
import org.koin.android.ext.android.inject

const val EXTRA_FORCE_ONBOARDING = "dev.bpavuk.touche.extra.FORCE_ONBOARDING"

class MainActivity : ComponentActivity() {
    private val onboardingRepository: OnboardingRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val forceOnboarding = intent.getBooleanExtra(
            EXTRA_FORCE_ONBOARDING,
            false
        )

        val onboard = !onboardingRepository.getOnboardingCompletedBlocking()

        setContent {
            ToucheTheme {
                AppRoot(
                    onboard = onboard || forceOnboarding,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
