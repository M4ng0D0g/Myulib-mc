package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

/**
 * [ECS Component] 定義容器的佈局行為。
 * 支援水平、垂直排列以及絕對錨點定位。
 */
data class FlexContainerComponent(
    // 修正：加入 ABSOLUTE 以支援 Canvas 錨點系統
    var direction: FlexDirection = FlexDirection.VERTICAL,

    // 修正：改為 Float 以確保座標運算精度 (與 ComputedTransform 一致)
    var spacing: Float = 0f,

    var columns: Int = 1, // 新增：網格列數

    // 主軸對齊 (Main Axis)
    var mainAlign: MainAxisAlignment = MainAxisAlignment.START,

    // 交叉軸對齊 (Cross Axis)
    var crossAlign: CrossAxisAlignment = CrossAxisAlignment.STRETCH
) : Component

/**
 * 佈局方向模式
 */
enum class FlexDirection {
    HORIZONTAL,
    VERTICAL,
    FLOW,
    GRID,
    STACK,
    ABSOLUTE, // 用於 Canvas：忽略順序，改用 Anchor 與 Offset 定位
}

/**
 * 主軸對齊方式 (用於水平/垂直排列)
 */
enum class MainAxisAlignment {
    START,
    CENTER,
    END,
    SPACE_BETWEEN,
    SPACE_AROUND
}

/**
 * 交叉軸對齊方式
 */
enum class CrossAxisAlignment {
    START,
    CENTER,
    END,
    STRETCH
}