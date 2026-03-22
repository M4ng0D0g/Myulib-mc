package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.text.Text

/**
 * [API] 分頁標籤按鈕。
 */
class TabButton(
    label: String,
    var isSelected: Boolean = false,
    onClick: () -> Unit
) : Button(onClick) {

    private val labelText = Text.literal(label)

    override fun draw(context: net.minecraft.client.gui.DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // 根據選中狀態切換背景顏色
        val baseColor = if (isSelected)
            UiSchema.Action.BUTTON_ACTIVE.argb
        else if (state.isHovered)
            UiSchema.Action.BUTTON_HOVER.argb
        else
            UiSchema.Action.BUTTON_IDLE.argb

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        // 繪製標籤外觀 (頂部圓角或特定裝飾)
        context.fill(x, y, x + w, y + h, UiSchema.Action.BORDER_NORMAL.argb)
        context.fill(x + 1, y + 1, x + w - 1, y + h, baseColor) // 底部不留邊框，產生連接感

        // 繪製文字
        val tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer
        val textW = tr.getWidth(labelText)
        context.drawText(tr, labelText, x + (w - textW) / 2, y + (h - 8) / 2, 0xFFFFFFFF.toInt(), false)
    }
}