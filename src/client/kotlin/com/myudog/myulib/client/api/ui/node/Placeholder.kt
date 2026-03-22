package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.data.Color
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.gui.DrawContext
import java.util.concurrent.atomic.AtomicInteger

/**
 * [API] 佈局佔位符。
 * 用於在開發階段模擬元件尺寸，會自動循環切換色彩以便辨識。
 */
class Placeholder(
    width: Float = 20f,
    height: Float = 20f,
    var isDebug: Boolean = true
) : LeafWidget() {

    // 實體唯一的顏色，從調色盤中選取
    private val debugColor: Color

    companion object {
        // 內建調色盤，使用 UiSchema 的色盤
        private val DEBUG_PALETTE = listOf(
            UiSchema.Palette.RED,
            UiSchema.Palette.BLUE,
            UiSchema.Palette.PURPLE,
            Color.fromHex("#FFB74D"), // Orange-ish
            Color.fromHex("#81C784"), // Green-ish
            Color.fromHex("#FF8A65")  // Coral
        )
        private val paletteIndex = AtomicInteger(0)
    }

    init {
        // 設定初始尺寸
        transform.width = SizeUnit.Fixed(width)
        transform.height = SizeUnit.Fixed(height)

        // 自動分配顏色
        val idx = paletteIndex.getAndIncrement()
        debugColor = DEBUG_PALETTE[idx % DEBUG_PALETTE.size]
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        // 1. 繪製主色塊
        val color = if (isDebug) debugColor.argb else 0xFFAAAAAA.toInt()
        context.fill(x, y, x + w, y + h, color)

        // 2. 繪製輔助邊框 (如果處於 Debug 模式)
        if (isDebug) {
            val borderColor = 0xFF333333.toInt()
            context.fill(x, y, x + w, y + 1, borderColor)         // Top
            context.fill(x, y + h - 1, x + w, y + h, borderColor) // Bottom
            context.fill(x, y, x + 1, y + h, borderColor)         // Left
            context.fill(x + w - 1, y, x + w, y + h, borderColor) // Right
        }
    }
}