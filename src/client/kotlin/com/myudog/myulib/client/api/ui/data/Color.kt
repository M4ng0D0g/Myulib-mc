package com.myudog.myulib.client.api.ui.data

/**
 * [API] 輕量級色彩封裝 (使用 Inline Value Class 避免記憶體開銷)
 */
@JvmInline
value class Color(val argb: Int) {
    companion object {
        fun of(argb: Int) = Color(argb)
        fun fromHex(hex: String): Color {
            val cleanHex = hex.removePrefix("#")
            return Color(cleanHex.toLong(16).toInt())
        }
    }

    val alpha get() = (argb ushr 24) and 0xFF
    val red   get() = (argb ushr 16) and 0xFF
    val green get() = (argb ushr 8) and 0xFF
    val blue  get() = argb and 0xFF

    fun withAlpha(newAlpha: Int): Color {
        return Color((argb and 0x00FFFFFF) or (newAlpha shl 24))
    }
}