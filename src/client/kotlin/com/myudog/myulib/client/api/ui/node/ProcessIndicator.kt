package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import java.util.function.Supplier

enum class IndicatorShape { ARROW_RIGHT, ARROW_DOWN, FLAME, BUBBLE, BAR_HORIZONTAL }

/**
 * [API] 處理進度指示器。
 * 適用於熔爐進度、能源條或任何百分比顯示。
 */
class ProcessIndicator(val shape: IndicatorShape) : LeafWidget() {

    var progressSupplier: Supplier<Float> = Supplier { 0f }
    var isActive: Boolean = false
    private var animationTick = 0

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return
        animationTick++

        val progress = progressSupplier.get().coerceIn(0f, 1f)
        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()
        val tr = MinecraftClient.getInstance().textRenderer

        when (shape) {
            IndicatorShape.ARROW_RIGHT -> {
                // 渲染灰色底色箭頭
                context.drawText(tr, "→", x + 4, y + (h - 8) / 2, UiSchema.Action.BORDER_DARK.argb, false)
                if (progress > 0) {
                    val fillW = (w * progress).toInt()
                    // 根據 active 狀態切換顏色
                    val color = if (isActive) 0xFFFFAA00.toInt() else UiSchema.Text.PRIMARY.argb
                    context.fill(x, y + h - 3, x + fillW, y + h - 1, color)
                }
            }
            IndicatorShape.FLAME -> renderFlame(context, x, y, w, h, progress)
            IndicatorShape.BUBBLE -> renderBubbles(context, x, y, w, h, progress)
            else -> {} // 其他形狀可依此類推
        }
    }

    private fun renderFlame(context: DrawContext, x: Int, y: Int, w: Int, h: Int, progress: Float) {
        val flameH = (h * progress).toInt()
        context.fill(x + 2, y + 2, x + w - 2, y + h - 2, 0x80000000.toInt())
        if (flameH > 0) {
            val color = if (isActive) 0xFFFFAA00.toInt() else 0xFFFF6600.toInt()
            context.fill(x + 3, y + h - 2 - flameH, x + w - 3, y + h - 2, color)
        }
    }

    private fun renderBubbles(context: DrawContext, x: Int, y: Int, w: Int, h: Int, progress: Float) {
        if (isActive && progress > 0) {
            val offset = (animationTick / 4) % 8
            for (i in 0..2) {
                val by = y + h - 6 - (i * 8) - offset
                if (by > y + 2 && by < y + h - 4) {
                    context.fill(x + 3, by, x + w - 3, by + 2, 0xFF88CCFF.toInt())
                }
            }
        }
    }
}