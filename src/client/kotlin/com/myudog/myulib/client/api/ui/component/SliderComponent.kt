package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

class SliderComponent(
    var value: Double = 0.5,
    var min: Double = 0.0,
    var max: Double = 1.0,
    var step: Double = 0.01,
    var isDragging: Boolean = false,
    var onValueChanged: (Double) -> Unit = {}
) : Component {
    // 計算當前百分比 (0.0 ~ 1.0)
    val percentage: Float
        get() = ((value - min) / (max - min)).coerceIn(0.0, 1.0).toFloat()
}