package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.render.TextureRegistry
import com.myudog.myulib.client.api.render.TextureType
import com.myudog.myulib.client.api.ui.ContainerWidget
import com.myudog.myulib.client.api.ui.data.UiSchema
import com.myudog.myulib.client.gui.widgets.base.NineSliceRenderer
import com.myudog.myulib.client.internal.ui.render.SeparatorUtility
import net.minecraft.client.gui.DrawContext

/**
 * [API] 萬用容器元件。
 * 支援透過 textureKey 從 TextureRegistry 自動獲取 9-Slice、3-Slice 或普通圖片進行渲染。
 */
open class Box : ContainerWidget() {

    // K-V 材質索引，類似 CSS 的 ClassName
    var textureKey: String? = null

    // 支援除錯模式
    enum class RenderMode { PRODUCTION, OUTLINE, COLOR_BLOCKS }
    var renderMode: RenderMode = RenderMode.PRODUCTION

    // 渲染快取 (針對 9-Slice 優化)
    private var cachedParams: NineSliceRenderer.DrawParams? = null
    private var lastKey: String? = null
    private var lastSize: Pair<Int, Int>? = null

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        if (renderMode != RenderMode.PRODUCTION) {
            drawDebug(context, x, y, w, h)
            return
        }

        // 核心邏輯：根據 textureKey 渲染
        val key = textureKey
        if (key == null) {
            // 如果沒給 Key，畫一個半透明背景作為預設占位
            context.fill(x, y, x + w, y + h, UiSchema.Surface.PANEL.argb)
            return
        }

        val meta = TextureRegistry.get(key)
        if (meta == null) {
            // 如果 Key 找不到，畫出 Placeholder 顏色以便 Debug
            context.fill(x, y, x + w, y + h, UiSchema.Palette.RED.withAlpha(0x80).argb)
            return
        }

        when (meta.type) {
            TextureType.NINE_SLICE -> renderNineSlice(context, key, x, y, w, h)
            TextureType.THREE_SLICE_H, TextureType.THREE_SLICE_V -> {
                // 這裡可以透過風格檔對接 SeparatorUtility (省略部分細節)
            }
            TextureType.SIMPLE -> {
                context.drawTexture(
                    net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,
                    meta.id,
                    x, y,
                    0f, 0f,
                    w, h,
                    meta.width, meta.height,
                    meta.width, meta.height
                )
            }
        }
    }

    private fun renderNineSlice(ctx: DrawContext, key: String, x: Int, y: Int, w: Int, h: Int) {
        // 快取機制：只有當 Key 改變或尺寸改變時才重算 UV 頂點
        if (cachedParams == null || lastKey != key || lastSize != (w to h)) {
            val nineSlice = TextureRegistry.toNineSlice(key) ?: return
            cachedParams = NineSliceRenderer.buildDrawParams(nineSlice, x, y, w, h)
            lastKey = key
            lastSize = (w to h)
        }

        val tex = TextureRegistry.get(key)?.id ?: return
        // 由於 NineSliceRenderer 需要 SingleTextureNineSlice 對象，這裡傳入對應資源
        NineSliceRenderer.executeDrawParams(ctx, TextureRegistry.toNineSlice(key), cachedParams)
    }

    private fun drawDebug(ctx: DrawContext, x: Int, y: Int, w: Int, h: Int) {
        if (renderMode == RenderMode.OUTLINE) {
            val color = if (state.isHovered) 0xFFFFFF00.toInt() else 0xFFFFFFFF.toInt()
            drawDebugOutline(ctx, x, y, w, h, color)
        } else if (renderMode == RenderMode.COLOR_BLOCKS) {
            ctx.fill(x, y, x + w, y + h, 0x44FF0000.toInt())
        }
    }

    private fun drawDebugOutline(ctx: DrawContext, x: Int, y: Int, w: Int, h: Int, color: Int) {
        ctx.fill(x, y, x + w, y + 1, color)
        ctx.fill(x, y + h - 1, x + w, y + h, color)
        ctx.fill(x, y + 1, x + 1, y + h - 1, color)
        ctx.fill(x + w - 1, y + 1, x + w, y + h - 1, color)
    }

    // --- Fluent API ---
    fun withTexture(key: String): Box {
        this.textureKey = key
        return this
    }
}