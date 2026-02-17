package dev.bpavuk.touche

import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import dev.bpavuk.touche.data.UsbConnectionViewModel
import dev.bpavuk.touche.data.UsbDisconnectBroadcastReceiver
import dev.bpavuk.touche.logic.input.InputViewModel
import dev.bpavuk.touche.logic.input.InputViewModelImpl
import dev.bpavuk.touche.ui.StylusSurface
import dev.bpavuk.touche.ui.theme.ToucheTheme
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

const val TAG = "Surface"

class SurfaceActivity : ComponentActivity() {
    private val usbDisconnectReceiver = UsbDisconnectBroadcastReceiver { _, _ ->
        finish()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.hide(WindowInsetsCompat.Type.captionBar())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        Log.d(TAG, "intent:")
        Log.d(TAG, "\taction: ${intent.action}")
        Log.d(TAG, "\ttype: ${intent.type}")

        val usbVm = try {
            ViewModelProvider(
                owner = this,
                factory = UsbConnectionViewModel.factoryFrom(this)
            )[UsbConnectionViewModel::class.java]
        } catch (e: IllegalStateException) {
            Log.d(TAG, "USB VM creation failed: ${e.message}")
            finish()
            return
        }
        val connection = usbVm.connection

        val inputViewModel: InputViewModel by viewModel<InputViewModelImpl> { parametersOf(connection) }

        ContextCompat.registerReceiver(
            this,
            usbDisconnectReceiver,
            IntentFilter(UsbManager.ACTION_USB_ACCESSORY_DETACHED),
            ContextCompat.RECEIVER_EXPORTED
        )

        setContent {
            ToucheTheme {
                Box(Modifier
                     .background(Color.Black)
                     .navigationBarsPadding()
                ) {
                    StylusSurface( // TODO: replace with [Tablet]
                        inputViewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(usbDisconnectReceiver)
    }
}