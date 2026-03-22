package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

/**
 * [API] 儲存 Checkbox 的開關狀態與事件回調。
 */
class CheckboxComponent(
    var isChecked: Boolean = false,
    var onToggle: (Boolean) -> Unit = {}
) : Component