package dev.bpavuk.touche

import android.content.IntentFilter
import android.hardware.usb.UsbAccessory
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import dev.bpavuk.touche.input.InputViewModel
import dev.bpavuk.touche.ui.StylusSurface
import dev.bpavuk.touche.data.ToucheRepositoryImpl
import dev.bpavuk.touche.data.UsbConnection
import dev.bpavuk.touche.data.UsbDisconnectBroadcastReceiver
import dev.bpavuk.touche.ui.theme.ToucheTheme
import kotlinx.serialization.ExperimentalSerializationApi

const val TAG = "Surface"

class SurfaceActivity : ComponentActivity() {
    private lateinit var connection: UsbConnection
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

        if (intent.action != UsbManager.ACTION_USB_ACCESSORY_ATTACHED) finish()
        val usbManager = getSystemService(USB_SERVICE) as UsbManager
        val accessory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY, UsbAccessory::class.java)
        } else {
            (intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY) as? UsbAccessory)
        } ?: let {
            Log.d(TAG, "Did not receive the USB accessory!")
            finish()
            return
        }

        connection = UsbConnection(accessory, usbManager).apply { open() }
        val inputViewModel = InputViewModel(
            ToucheRepositoryImpl(connection)
        )

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
                    StylusSurface(
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

        connection.close()
        unregisterReceiver(usbDisconnectReceiver)
    }
}