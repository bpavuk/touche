package dev.bpavuk.touche

import android.content.Context
import android.content.IntentFilter
import android.hardware.usb.UsbAccessory
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
import dev.bpavuk.touche.input.InputViewModel
import dev.bpavuk.touche.input.StylusSurface
import dev.bpavuk.touche.input.WatcherViewModel
import dev.bpavuk.touche.ui.theme.ToucheTheme
import dev.bpavuk.touche.usb.UsbConnection
import dev.bpavuk.touche.usb.UsbDisconnectBroadcastReceiver

const val TAG = "Surface"

class SurfaceActivity : ComponentActivity() {
    private var connection: UsbConnection? = null
    private val usbDisconnectBroadcastReceiver = UsbDisconnectBroadcastReceiver { _, _ ->
        finish()
    }

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
        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val accessory =
            (intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY) as? UsbAccessory) ?: let {
                finish()
                return
            }

        connection = UsbConnection(accessory, usbManager).apply { open() }
        val inputViewModel = InputViewModel(
            WatcherViewModel(connection!!)
        )

        ContextCompat.registerReceiver(
            this,
            usbDisconnectBroadcastReceiver,
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

        connection?.close()
        unregisterReceiver(usbDisconnectBroadcastReceiver)
    }
}