package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.BaseWidget

class Stack : Box() {
    // Provide children access for higher-level components
    val children: MutableList<BaseWidget>
        get() = (this as Box).let { // use underlying hierarchy
            val h = com.myudog.myulib.client.api.MyulibApiClient.getComponent<com.myudog.myulib.client.api.ui.component.HierarchyComponent>(this.entityId)
            val list = h?.children ?: mutableListOf()
            // map ids to widget instances
            val widgets = mutableListOf<BaseWidget>()
            for (id in list) {
                val inst = com.myudog.myulib.client.api.MyulibApiClient.getComponent<com.myudog.myulib.client.api.ui.component.WidgetInstanceComponent>(id)?.instance
                inst?.let { widgets.add(it) }
            }
            widgets
        }

    fun addChildWidget(w: BaseWidget) = addChild(w)
}

