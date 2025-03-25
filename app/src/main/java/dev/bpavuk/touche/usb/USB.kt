package dev.bpavuk.touche.usb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbAccessory
import android.hardware.usb.UsbManager
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

const val TAG = "Usb"

class UsbConnection(
    private val accessory: UsbAccessory, private val manager: UsbManager
) {
    private var fileDescriptor: ParcelFileDescriptor? = null
    private var inputStream: FileInputStream? = null
    private var outputStream: FileOutputStream? = null

    fun open() {
        Log.d(TAG, "opening accessory")
        fileDescriptor = manager.openAccessory(accessory)
        fileDescriptor?.fileDescriptor?.also { fd ->
            inputStream = FileInputStream(fd)
            outputStream = FileOutputStream(fd)
        }
    }

    fun close() {
        Log.d(TAG, "closing accessory")
        inputStream?.close()
        outputStream?.close()
        fileDescriptor?.close()

        inputStream = null
        outputStream = null
        fileDescriptor = null
    }

    suspend fun read(): Int {
        return withContext(Dispatchers.IO) {
            return@withContext inputStream?.read()
                ?: throw IOException("Attempt to read from closed USB connection")
        }
    }

    suspend fun read(b: ByteArray): Int {
        return withContext(Dispatchers.IO) {
            return@withContext inputStream?.read(b)
                ?: throw IOException("Attempt to read from closed USB connection")
        }
    }

    suspend fun readBytes(): ByteArray {
        return withContext(Dispatchers.IO) {
            Log.d("USBTest", "read performing")
            return@withContext inputStream?.readBytes()
                ?: throw IOException("Attempt to read from closed USB connection")
        }
    }

    fun readBlocking(): Int {
        return inputStream?.read()
            ?: throw IOException("Attempt to read from closed USB connection")
    }

    fun readBlocking(b: ByteArray): Int {
        return inputStream?.read(b)
            ?: throw IOException("Attempt to read from closed USB connection")
    }

    fun readBytesBlocking(): ByteArray {
        return inputStream?.readBytes()
            ?: throw IOException("Attempt to read from closed USB connection")
    }

    suspend fun write(b: Int) {
        withContext(Dispatchers.IO) {
            outputStream?.write(b) ?: throw IOException("Attempt to write to closed USB connection")
        }
    }

    suspend fun write(b: ByteArray) {
        withContext(Dispatchers.IO) {
            outputStream?.write(b) ?: throw IOException("Attempt to write to closed USB connection")
        }
    }

    suspend fun write(b: ByteArray, off: Int, len: Int) {
        withContext(Dispatchers.IO) {
            outputStream?.write(b, off, len)
                ?: throw IOException("Attempt to write to closed USB connection")
        }
    }

    fun writeBlocking(b: Int) {
        outputStream?.write(b) ?: throw IOException("Attempt to write to closed USB connection")
    }

    fun writeBlocking(b: ByteArray) {
        outputStream?.write(b) ?: throw IOException("Attempt to write to closed USB connection")
    }

    fun writeBlocking(b: ByteArray, off: Int, len: Int) {
        outputStream?.write(b, off, len)
            ?: throw IOException("Attempt to write to closed USB connection")
    }
}

class UsbDisconnectBroadcastReceiver(
    private val onDisconnect: (Context?, Intent?) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == UsbManager.ACTION_USB_ACCESSORY_DETACHED) onDisconnect(
            context,
            intent
        )
        else Log.d(TAG, "unreceived action")
    }
}