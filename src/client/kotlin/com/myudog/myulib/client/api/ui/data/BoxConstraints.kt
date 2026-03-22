package com.myudog.myulib.client.api.ui.data

/**
 * [API] 佈局約束。
 * 定義了元件允許的尺寸範圍。
 */
data class BoxConstraints(
    val minWidth: Float = 0f,
    val maxWidth: Float = Float.POSITIVE_INFINITY,
    val minHeight: Float = 0f,
    val maxHeight: Float = Float.POSITIVE_INFINITY
) {
    /** 確保尺寸在限制範圍內 */
    fun constrainWidth(width: Float) = width.coerceIn(minWidth, maxWidth)
    fun constrainHeight(height: Float) = height.coerceIn(minHeight, maxHeight)
}