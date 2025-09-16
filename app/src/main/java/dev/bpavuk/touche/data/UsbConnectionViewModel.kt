package dev.bpavuk.touche.data

import android.app.Activity
import android.hardware.usb.UsbAccessory
import android.hardware.usb.UsbManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel that owns a UsbConnection so it survives configuration changes.
 * The connection is created once and closed in onCleared when the Activity is truly finishing.
 */
class UsbConnectionViewModel(
    val connection: UsbConnection
) : ViewModel() {

    override fun onCleared() {
        connection.close()
    }

    companion object {
        fun factoryFrom(activity: Activity): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UsbConnectionViewModel::class.java).not()) {
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }

                val intent = activity.intent
                if (intent.action != UsbManager.ACTION_USB_ACCESSORY_ATTACHED) {
                    throw IllegalStateException("Invalid intent for creating UsbConnectionViewModel")
                }
                val usbManager = activity.getSystemService(Activity.USB_SERVICE) as UsbManager
                val accessory: UsbAccessory? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY, UsbAccessory::class.java)
                } else {
                    intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY) as? UsbAccessory
                }
                val acc = accessory ?: throw IllegalStateException("USB accessory missing in intent")

                val connection = UsbConnection(acc, usbManager).apply { open() }
                return UsbConnectionViewModel(connection) as T
            }
        }
    }
}
