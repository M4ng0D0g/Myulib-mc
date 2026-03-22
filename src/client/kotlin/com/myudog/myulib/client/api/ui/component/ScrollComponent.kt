package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

/**
 * [ECS Component] 儲存捲動區域的狀態。
 */
class ScrollComponent(
    var scrollAmount: Float = 0f,
    var contentHeight: Float = 0f,
    var isDraggingThumb: Boolean = false,
    var dragStartMouseY: Float = 0f,
    var dragStartScrollAmount: Float = 0f,
    var needsScrollbar: Boolean = true,
    var bottomGap: Float = 0f
) : Component {
    val maxScroll: Float
        get() = (contentHeight - 0f).coerceAtLeast(0f) // 0f 會在 Layout 階段被 ViewportH 更新
}