package com.myudog.myulib.client.api.ui

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.component.WidgetStateComponent

/**
 * [API] 功能型元件 (如 Button, Label)。
 * 具備狀態 (Visible/Enabled)，但禁止擁有子元件。
 */
abstract class LeafWidget : BaseWidget() {
    init {
        // state component is provided by BaseWidget
    }

    // 遮蔽 addChild，確保 LeafWidget 不會被當作容器使用
    @Deprecated("LeafWidget cannot have children", level = DeprecationLevel.ERROR)
    fun addChild(child: BaseWidget) = Unit
}