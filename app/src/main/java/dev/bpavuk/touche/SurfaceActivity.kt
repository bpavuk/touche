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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import dev.bpavuk.touche.input.InputViewModel
import dev.bpavuk.touche.input.StylusSurface
import dev.bpavuk.touche.input.UsbInputHandler
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
        Log.d(TAG, "intent:")
        Log.d(TAG, "\taction: ${intent.action}")
        Log.d(TAG, "\ttype: ${intent.type}")

        if (intent.action != UsbManager.ACTION_USB_ACCESSORY_ATTACHED) finish()
        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val accessory = (intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY) as? UsbAccessory)
            ?: let {
                finish()
                return
            }

        connection = UsbConnection(accessory, usbManager).apply { open() }
        val inputViewModel = InputViewModel(UsbInputHandler(connection!!))

        ContextCompat.registerReceiver(
            this,
            usbDisconnectBroadcastReceiver,
            IntentFilter(UsbManager.ACTION_USB_ACCESSORY_DETACHED),
            ContextCompat.RECEIVER_EXPORTED
        )

        setContent {
            ToucheTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StylusSurface(
                        inputViewModel,
                        modifier = Modifier
                            .padding(innerPadding)
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