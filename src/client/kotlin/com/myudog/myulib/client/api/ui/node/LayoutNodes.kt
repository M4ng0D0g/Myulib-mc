package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.component.FlexDirection

/**
 * 垂直排列容器
 */
open class Column : Box() {
    init {
        flexContainer.direction = FlexDirection.VERTICAL
    }
}

/**
 * 水平排列容器
 */
open class Row : Box() {
    init {
        flexContainer.direction = FlexDirection.HORIZONTAL
    }
}