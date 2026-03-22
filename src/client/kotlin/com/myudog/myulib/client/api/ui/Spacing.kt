package com.myudog.myulib.client.api.ui

data class Spacing(val left: Int, val right: Int, val top: Int, val bottom: Int) {
    val horizontal: Int get() = left + right
    val vertical: Int get() = top + bottom

    operator fun plus(other: Spacing) = Spacing(left + other.left, right + other.right, top + other.top, bottom + other.bottom)

    companion object {
        fun zero() = Spacing(0, 0, 0, 0)
        fun all(value: Int) = Spacing(value, value, value, value)
    }
}
