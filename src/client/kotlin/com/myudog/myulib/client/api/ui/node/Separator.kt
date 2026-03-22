package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.data.SeparatorStyle
import com.myudog.myulib.client.api.ui.data.UiSchema
import com.myudog.myulib.client.internal.ui.render.SeparatorUtility
import net.minecraft.client.gui.DrawContext

/**
 * [API] 分割線元件。
 * 整合了舊版 SeparatorWidget 的純色線條與新版的材質線條。
 */
class Separator(val style: SeparatorStyle? = null) : LeafWidget() {

    // 如果沒有設定 style，則使用純色模式
    var color: Int = UiSchema.Action.BORDER_NORMAL.argb
    var isVertical: Boolean = false
    var thickness: Float = 1f

    init {
        // 1. 根據樣式自動初始化尺寸
        if (style != null) {
            if (style.isVertical) {
                transform.width = SizeUnit.Fixed(style.texW.toFloat())
                transform.height = SizeUnit.FillContainer
            } else {
                transform.width = SizeUnit.FillContainer
                transform.height = SizeUnit.Fixed(style.texH.toFloat())
            }
        } else {
            // 2. 純色模式的預設尺寸
            if (isVertical) {
                transform.width = SizeUnit.Fixed(thickness)
                transform.height = SizeUnit.FillContainer
            } else {
                transform.width = SizeUnit.FillContainer
                transform.height = SizeUnit.Fixed(thickness)
            }
        }
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        if (style != null) {
            // 材質渲染路徑
            SeparatorUtility.draw(context, style, x, y, w, h)
        } else {
            // 純色渲染路徑 (舊版 SeparatorWidget 邏輯)
            context.fill(x, y, x + w, y + h, color)
        }
    }
}