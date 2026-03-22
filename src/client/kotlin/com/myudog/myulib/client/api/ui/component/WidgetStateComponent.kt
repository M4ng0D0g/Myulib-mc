package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

data class WidgetStateComponent(
    var isVisible: Boolean = true,
    var isEnabled: Boolean = true,
    var isHovered: Boolean = false,
    var isFocused: Boolean = false,
) : Component