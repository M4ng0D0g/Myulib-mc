package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.gui.DrawContext
import java.util.function.Supplier

/**
 * [API] 屬性狀態條 (血條/魔力條/經驗條)。
 * 支援數值緩動動畫與動態顏色切換。
 */
class AttributeBar(
    private val valueSupplier: Supplier<Float>, // 當前值
    private val maxSupplier: Supplier<Float>,   // 最大值
    var barColor: Int = 0xFF00FF00.toInt(),     // 預設顏色
    var showText: Boolean = true                // 是否顯示數字 (如 20/20)
) : LeafWidget() {

    // 渲染用的平滑數值 (用於動畫)
    private var renderValue: Float = -1f
    private val lerpSpeed = 0.15f // 緩動速度 (0~1)

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val current = valueSupplier.get()
        val max = maxSupplier.get().coerceAtLeast(1f)

        // 初始設定渲染值
        if (renderValue < 0) renderValue = current

        // ── 觀念解釋：線性插值 (Linear Interpolation / Lerp) ──
        // 為了讓數值變動更平滑，我們不直接設定渲染值，而是每一幀向目標靠近一點：
        // $$V_{render} = V_{render} + (V_{target} - V_{render}) \times \alpha$$
        renderValue += (current - renderValue) * lerpSpeed

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        // 1. 繪製背景與邊框 (從 UiSchema 讀取)
        context.fill(x, y, x + w, y + h, UiSchema.Action.BORDER_DARK.argb)
        context.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xFF202020.toInt())

        // 2. 繪製進度內容
        if (max > 0) {
            val progressWidth = ((w - 2) * (renderValue / max)).toInt().coerceIn(0, w - 2)

            // 根據血量百分比動態調整顏色 (例如低於 20% 變紅)
            val finalColor = if (renderValue / max < 0.2f) 0xFFFF4444.toInt() else barColor

            context.fill(x + 1, y + 1, x + 1 + progressWidth, y + h - 1, finalColor)
        }

        // 3. 繪製文字 (20 / 20)
        if (showText) {
            val client = net.minecraft.client.MinecraftClient.getInstance()
            val text = "${current.toInt()} / ${max.toInt()}"
            val textW = client.textRenderer.getWidth(text)
            context.drawText(client.textRenderer, text, x + (w - textW) / 2, y + (h - 8) / 2, 0xFFFFFFFF.toInt(), false)
        }
    }
}