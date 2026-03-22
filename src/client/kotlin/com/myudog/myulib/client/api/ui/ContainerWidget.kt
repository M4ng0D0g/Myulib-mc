package com.myudog.myulib.client.api.ui

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.component.FlexContainerComponent
import com.myudog.myulib.client.api.ui.component.HierarchyComponent

/**
 * [API] 容器型元件 (如 Panel, Column, Row)。
 * 具備 Flex 排版屬性，可管理子元件。
 */
abstract class ContainerWidget : BaseWidget() {
    init {
        // 容器特有的組件：決定子元件如何排列
        MyulibApiClient.addComponent(entityId, FlexContainerComponent())
    }

    val flexContainer: FlexContainerComponent get() = MyulibApiClient.getComponent(entityId)!!

    open fun addChild(child: BaseWidget) {
        child.setParent(this)
    }

    fun addChildren(vararg children: BaseWidget) {
        children.forEach { it.setParent(this) }
    }

    /** Draw all children in z-order. This helper is used by ScrollBox and others. */
    protected fun drawChildren(context: net.minecraft.client.gui.DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val hierarchy = MyulibApiClient.getComponent<HierarchyComponent>(entityId) ?: return
        for (childId in hierarchy.children) {
            val widget = MyulibApiClient.getComponent<com.myudog.myulib.client.api.ui.component.WidgetInstanceComponent>(childId)?.instance
            widget?.draw(context, mouseX, mouseY, delta)
        }
    }
}