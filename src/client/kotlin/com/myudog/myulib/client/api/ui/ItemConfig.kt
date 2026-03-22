package com.myudog.myulib.client.api.ui

/** Simple padding holder with convenience setters used by UI nodes. */
class Padding(var left: Float = 0f, var right: Float = 0f, var top: Float = 0f, var bottom: Float = 0f) {
    fun set(all: Float) { left = all; right = all; top = all; bottom = all }
    fun set(l: Float, t: Float, r: Float, b: Float) { left = l; top = t; right = r; bottom = b }
}

class ItemConfig {
    val padding: Padding = Padding()
    var weight: Float = 0f
}

