package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

/**
 * [API] 標記元件為可點擊，並定義回調邏輯。
 */
class ClickableComponent(
    var onClick: (button: Int) -> Unit = {},
    var onHover: (isHovered: Boolean) -> Unit = {},
    var consumeEvent: Boolean = true // 是否攔截點擊，防止點到後方的元件
) : Component