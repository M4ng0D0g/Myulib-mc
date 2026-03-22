package com.myudog.myulib.client.api.ui

/**
 * 錨點位置：決定元件相對於父容器的對齊基準點
 */
enum class Anchor(val xFactor: Float, val yFactor: Float) {
    TOP_LEFT(0f, 0f),    TOP_CENTER(0.5f, 0f),    TOP_RIGHT(1f, 0f),
    CENTER_LEFT(0f, 0.5f), CENTER(0.5f, 0.5f),      CENTER_RIGHT(1f, 0.5f),
    BOTTOM_LEFT(0f, 1f), BOTTOM_CENTER(0.5f, 1f), BOTTOM_RIGHT(1f, 1f)
}