package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

class DropdownComponent(
    val options: MutableList<String> = mutableListOf(),
    var selectedIndex: Int = -1,
    var isExpanded: Boolean = false,
    var maxVisibleOptions: Int = 5,
    var onSelect: (Int) -> Unit = {}
) : Component {
    val selectedOption: String?
        get() = if (selectedIndex in options.indices) options[selectedIndex] else null
}