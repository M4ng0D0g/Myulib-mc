package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component
import com.myudog.myulib.client.api.ui.Anchor
import com.myudog.myulib.client.api.ui.BoxValues
import com.myudog.myulib.client.api.ui.SizeUnit

/**
 * [API] 變換組件：定義 UI 元件的佈局規則。
 * 這裡不儲存最終的像素座標，而是儲存「如何計算座標」的意圖。
 */
data class TransformComponent(
    var anchor: Anchor = Anchor.TOP_LEFT,
    var width: SizeUnit = SizeUnit.Fixed(100f),
    var height: SizeUnit = SizeUnit.Fixed(20f),
    var offsetX: Float = 0f,
    var offsetY: Float = 0f,
    // 內距與外距預留
    var padding: BoxValues = BoxValues.ZERO,
    var margin: BoxValues = BoxValues.ZERO
) : Component
