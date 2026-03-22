package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

/**
 * [API - ReadOnly] 存放佈局引擎計算後的絕對像素結果。
 * ⚠️ 注意：此組件由 LayoutSystem 自動更新，請勿手動修改其數值，否則會被覆蓋。
 */
data class ComputedTransform(
    var x: Float = 0f,
    var y: Float = 0f,
    var w: Float = 0f,
    var h: Float = 0f
) : Component