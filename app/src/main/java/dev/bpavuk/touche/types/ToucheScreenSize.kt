package dev.bpavuk.touche.types

data class ToucheScreenSize(val x: Int, val y: Int) : ToucheData {
    override fun encode(): String = buildString {
        append("X\t")
        append("$x\t")
        append(y)
    }
}