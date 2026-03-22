package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.component.FlexDirection

/**
 * [API] 等分網格容器。
 * 強制所有子元件擁有相同的格子大小，並按指定列數排列。
 * 非常適合：背包 (Inventory)、快捷列、商店列表。
 */
open class Grid(columns: Int = 1, spacing: Float = 0f) : Box() {
    init {
        flexContainer.direction = FlexDirection.GRID
        flexContainer.columns = columns
        flexContainer.spacing = spacing
    }
}