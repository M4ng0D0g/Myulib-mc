package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

/**
 * [API] 文字標籤元件。
 * 整合了舊版 TextWidget 的功能，支援 Minecraft 的 Text 物件（語言包）。
 */
class Label(var content: Text) : LeafWidget() {

    // 輔助建構子：支援直接傳入 String
    constructor(text: String) : this(Text.literal(text))

    enum class Alignment { LEFT, CENTER, RIGHT }

    var color: Int = UiSchema.Text.PRIMARY.argb
    var alignment: Alignment = Alignment.LEFT
    var shadow: Boolean = true // 預設開啟陰影，提升 RPG 介面可讀性

    init {
        // 預設為 WrapContent，讓佈局引擎自動測量文字寬度
        transform.width = SizeUnit.WrapContent
        transform.height = SizeUnit.WrapContent
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val textRenderer = MinecraftClient.getInstance().textRenderer
        val textWidth = textRenderer.getWidth(content)

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        // 處理對齊邏輯
        val drawX = when (alignment) {
            Alignment.CENTER -> x + (w - textWidth) / 2
            Alignment.RIGHT -> x + (w - textWidth)
            Alignment.LEFT -> x
        }

        // 垂直置中
        val drawY = y + (h - 9) / 2

        context.drawText(textRenderer, content, drawX, drawY, color, shadow)
    }

    // --- Fluent API ---
    fun withColor(color: Int) = apply { this.color = color }
    fun withShadow(enabled: Boolean) = apply { this.shadow = enabled }
    fun centered() = apply { this.alignment = Alignment.CENTER }
    fun rightAligned() = apply { this.alignment = Alignment.RIGHT }

    // 更新內容的方法
    fun setText(newText: String) { this.content = Text.literal(newText) }
    fun setText(newText: Text) { this.content = newText }
}