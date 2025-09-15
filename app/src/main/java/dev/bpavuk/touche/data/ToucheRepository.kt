package dev.bpavuk.touche.data

import dev.bpavuk.touche.data.model.ToucheInput
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray

interface ToucheRepository {
    suspend fun sendEvent(event: ToucheInput)
    suspend fun sendEvents(events: List<ToucheInput>)
}

class ToucheRepositoryImpl @OptIn(ExperimentalSerializationApi::class) constructor(
    val usb: UsbConnection,
    val cbor: Cbor = Cbor {
        encodeValueTags = true
        encodeKeyTags = true
        encodeObjectTags = true
    }
) : ToucheRepository {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun sendEvent(event: ToucheInput) {
        sendEvents(listOf(event))
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun sendEvents(events: List<ToucheInput>) {
        val data = cbor.encodeToByteArray(events)
        usb.write(data)
    }
}
