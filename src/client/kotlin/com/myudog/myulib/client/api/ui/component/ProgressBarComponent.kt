package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component
import java.util.function.Supplier

class ProgressBarComponent(
    var targetProgress: Float = 0f,    // 目標進度 (0~1)
    var currentProgress: Float = 0f,   // 當前顯示進度 (動畫用)
    var ghostProgress: Float = 0f,     // 殘影進度 (受傷效果用)
    var lerpSpeed: Float = 0.1f,       // 動態平滑速度
    var direction: ProgressDirection = ProgressDirection.LEFT_TO_RIGHT,
    var progressSupplier: Supplier<Float>? = null
) : Component

enum class ProgressDirection {
    LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP
}