package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.component.ClickableComponent
import com.myudog.myulib.client.api.ui.component.DropdownComponent
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.gui.widgets.base.UIColors
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

class Dropdown(width: Float = 100f) : LeafWidget() {

    private val data = DropdownComponent()

    init {
        transform.width = SizeUnit.Fixed(width)
        transform.height = SizeUnit.Fixed(16f) // 固定高度與舊版一致

        MyulibApiClient.addComponent(entityId, data)

        // 註冊互動：處理主按鈕點擊
        MyulibApiClient.addComponent(entityId, ClickableComponent(
            onClick = {
                if (data.isExpanded) {
                    // 如果已展開，點擊主按鈕以外的地方交給 InputSystem 處理
                    // 這裡先處理切換
                    data.isExpanded = !data.isExpanded
                } else {
                    data.isExpanded = true
                }
            }
        ))
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        val textRenderer = MinecraftClient.getInstance().textRenderer

        // 1. 繪製主按鈕背景 (移植舊版 fill 邏輯)
        val bgColor = if (state.isHovered) UIColors.BUTTON_HOVER else UIColors.BUTTON_BG
        drawBox(context, x, y, w, h, bgColor, UIColors.BUTTON_BORDER)

        // 2. 繪製選中文字
        val displayText = data.selectedOption ?: "Select..."
        context.drawText(textRenderer, Text.literal(displayText), x + 4, y + 4, UIColors.TEXT_PRIMARY, false)

        // 3. 繪製箭頭
        val arrow = if (data.isExpanded) "▲" else "▼"
        context.drawText(textRenderer, Text.literal(arrow), x + w - 12, y + 4, UIColors.TEXT_SECONDARY, false)

        // 4. 繪製下拉列表 (如果展開)
        if (data.isExpanded && data.options.isNotEmpty()) {
            renderDropdownList(context, mouseX, mouseY)
        }
    }

    private fun renderDropdownList(context: DrawContext, mouseX: Int, mouseY: Int) {
        val x = computed.x.toInt()
        val listY = computed.y.toInt() + 16
        val w = computed.w.toInt()
        val textRenderer = MinecraftClient.getInstance().textRenderer

        val visibleCount = minOf(data.maxVisibleOptions, data.options.size)
        val listHeight = visibleCount * 14 + 2

        // 繪製列表背景
        drawBox(context, x, listY, w, listHeight, UIColors.BG_PANEL, UIColors.BORDER_NORMAL)

        // 繪製選項
        for (i in 0 until visibleCount) {
            val optionY = listY + 1 + (i * 14)
            val isHovered = mouseX >= x && mouseX < x + w && mouseY >= optionY && mouseY < optionY + 14

            if (isHovered) {
                context.fill(x + 1, optionY, x + w - 1, optionY + 14, UIColors.BUTTON_HOVER)
                // 這裡的小步驟：如果是點擊狀態，InputSystem 會處理，但渲染可以先做視覺反饋
            }

            context.drawText(textRenderer, Text.literal(data.options[i]), x + 4, optionY + 3, UIColors.TEXT_PRIMARY, false)
        }
    }

    private fun drawBox(ctx: DrawContext, x: Int, y: Int, w: Int, h: Int, bg: Int, border: Int) {
        ctx.fill(x, y, x + w, y + h, bg)
        // 繪製簡單邊框 (1px)
        ctx.fill(x, y, x + w, y + 1, border) // Top
        ctx.fill(x, y + h - 1, x + w, y + h, border) // Bottom
        ctx.fill(x, y, x + 1, y + h, border) // Left
        ctx.fill(x + w - 1, y, x + w, y + h, border) // Right
    }

    // --- Fluent API ---
    fun addOption(option: String) = apply { data.options.add(option) }
    fun onSelect(handler: (Int) -> Unit) = apply { data.onSelect = handler }
}