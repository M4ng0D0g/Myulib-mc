package com.myudog.myulib.client.api.ui.data

import net.minecraft.util.Identifier

/**
 * [API] 分割線樣式定義。
 */
enum class SeparatorStyle(
    val texture: Identifier,
    val texW: Int,
    val texH: Int,
    val capSize: Int,
    val isVertical: Boolean
) {}