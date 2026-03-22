package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component
import com.myudog.myulib.client.api.ui.BaseWidget

/**
 * [API] 舊版元件適配組件。
 * 用於在新的 ECS 系統中存儲並操作舊有的 Java GUI 元件。
 */
data class LegacyWidgetComponent(
    val instance: BaseWidget
) : Component