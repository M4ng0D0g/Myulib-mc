package com.myudog.myulib.client.internal.ui.render

import com.myudog.myulib.client.gui.enums.PanelStyle
import com.myudog.myulib.client.gui.widgets.base.NineSliceRenderer
import net.minecraft.client.gui.DrawContext

/**
 * [Internal Utility]
 * 負責將 PanelStyle 的數據轉化為 OpenGL 的渲染指令。
 */
object NineSliceUtility {

    /**
     * 對接你原有的渲染邏輯，但這裡我們只暴露最精簡的介面。
     */
    fun draw(context: DrawContext, style: PanelStyle, x: Int, y: Int, w: Int, h: Int) {
        // 這裡直接封裝你原本的 buildDrawParams 與 executeDrawParams 邏輯
        // 或者直接調用你原本的 Java 類別 (Kotlin 與 Java 互操作性極佳)
        NineSliceRenderer.drawTextured(context, style, x, y, w, h)
    }

    /**
     * 進階：配合 Box 的快取機制使用
     */
    fun drawCached(context: DrawContext, style: PanelStyle, params: NineSliceRenderer.DrawParams) {
        // Use compatible API: TextureRegistry.toNineSlice may return our SingleTextureNineSlice class
        NineSliceRenderer.executeDrawParams(context, style.singleTexture as? com.myudog.myulib.client.gui.widgets.base.NineSliceRenderer.SingleTextureNineSlice, params)
    }
}