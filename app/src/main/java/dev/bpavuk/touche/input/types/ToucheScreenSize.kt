package dev.bpavuk.touche.input.types

data class ToucheScreenSize(val x: Int, val y: Int) : ToucheData {
    override fun encode(): ByteArray = buildString {
        append("X\t")
        append("$x\t")
        append(y)
    }.encodeToByteArray()
}