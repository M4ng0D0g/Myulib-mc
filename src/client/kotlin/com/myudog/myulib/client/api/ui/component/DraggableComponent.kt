package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

/**
 * [ECS Component] 標記元件為可拖拽。
 */
class DraggableComponent(
    var isDragging: Boolean = false,
    var dragStartMouseX: Float = 0f,
    var dragStartMouseY: Float = 0f,
    var initialOffsetX: Float = 0f,
    var initialOffsetY: Float = 0f,

    // 拖拽邊界限制 (null 表示不限制)
    var minX: Float? = null,
    var minY: Float? = null,
    var maxX: Float? = null,
    var maxY: Float? = null,

    // 是否限制在父元件範圍內
    var lockInParent: Boolean = false
) : Component