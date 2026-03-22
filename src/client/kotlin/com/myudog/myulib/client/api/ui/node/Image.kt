package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

/**
 * [API] 圖片元件。
 * 支援自動測量貼圖尺寸與 Debug 佔位色塊。
 */
class Image(
    var texture: Identifier? = null,
    var u: Int = 0,
    var v: Int = 0,
    var regionW: Int = 16,
    var regionH: Int = 16,
    var textureW: Int = 256,
    var textureH: Int = 256
) : LeafWidget() {

    // Debug 開關：如果為 true，則只畫色塊不畫貼圖
    var debugMode: Boolean = false

    init {
        // 預設為 WrapContent，佈局引擎會根據 regionW/H 來測量
        transform.width = SizeUnit.WrapContent
        transform.height = SizeUnit.WrapContent
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        val tex = texture
        if (debugMode || tex == null) {
            // 💡 使用你定義在 UiSchema 中的佔位顏色進行 Debug
            context.fill(x, y, x + w, y + h, UiSchema.ICON_PLACEHOLDER.argb)
            return
        }

        // 正常渲染：支援拉伸以符合佈局分配的寬高
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            tex,
            x, y,
            u.toFloat(), v.toFloat(),
            regionW, regionH,
            regionW, regionH,
            textureW, textureH
        )
    }

    // --- Fluent API ---
    fun withUV(u: Int, v: Int, w: Int, h: Int) = apply {
        this.u = u; this.v = v; this.regionW = w; this.regionH = h
    }

    fun withDebug(enabled: Boolean) = apply { this.debugMode = enabled }
}