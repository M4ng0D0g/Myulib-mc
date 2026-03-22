package com.myudog.myulib.client.api.ui


/**
 * 用於儲存上下左右的數值（Padding/Margin）
 */
data class BoxValues(val top: Float, val bottom: Float, val left: Float, val right: Float) {
    companion object {
        val ZERO = BoxValues(0f, 0f, 0f, 0f)
    }
}