package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.component.ScrollComponent
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.gui.DrawContext

/**
 * [API] 捲動容器。
 * 核心原理：透過裁剪 (Scissor) 隱藏邊框外的子元件，並根據捲動值偏移座標。
 */
open class ScrollBox : Box() {
    val scrollData = ScrollComponent()

    init {
        MyulibApiClient.addComponent(entityId, scrollData)
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        // 1. 畫背景
        super.draw(context, mouseX, mouseY, delta)

        // 2. 開啟 Scissor (裁剪區域為 Box 的內部空間)
        // 注意：Minecraft 的 Scissor 使用螢幕座標且會受縮放影響
        context.enableScissor(x, y, x + w, y + h)

        // 3. 繪製子元件 (它們的座標已被 LayoutSystem 偏移)
        drawChildren(context, mouseX, mouseY, delta)

        context.disableScissor()

        // 4. 畫捲軸 (在裁剪區外畫，確保不會被切掉)
        if (scrollData.needsScrollbar && scrollData.contentHeight > h) {
            drawScrollbar(context, x, y, w, h)
        }
    }

    private fun drawScrollbar(context: DrawContext, x: Int, y: Int, w: Int, h: Int) {
        val barW = 4
        val barX = x + w - barW - 2
        val maxScroll = scrollData.contentHeight - h

        // 計算捲軸高度與位置
        val ratio = h / scrollData.contentHeight
        val thumbH = (h * ratio).coerceAtLeast(10f).toInt()
        val thumbY = y + (scrollData.scrollAmount / maxScroll * (h - thumbH)).toInt()

        context.fill(barX, thumbY, barX + barW, thumbY + thumbH, UiSchema.Action.BUTTON_BORDER.argb)
    }
}