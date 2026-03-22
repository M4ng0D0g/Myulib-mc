package com.myudog.myulib.client.internal.ui.render

import com.myudog.myulib.client.api.ui.data.SeparatorStyle
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext

object SeparatorUtility {

    fun draw(context: DrawContext, style: SeparatorStyle, x: Int, y: Int, w: Int, h: Int) {
        if (style.isVertical) {
            drawVertical(context, style, x, y, h)
        } else {
            drawHorizontal(context, style, x, y, w)
        }
    }

    private fun drawHorizontal(ctx: DrawContext, s: SeparatorStyle, x: Int, y: Int, totalW: Int) {
        val midTexW = s.texW - 2 * s.capSize
        val midW = totalW - 2 * s.capSize

        // Left Cap
        drawUv(ctx, s, x, y, s.capSize, s.texH, 0, 0, s.capSize, s.texH)
        // Middle (Stretched)
        if (midW > 0) {
            drawUv(ctx, s, x + s.capSize, y, midW, s.texH, s.capSize, 0, midTexW, s.texH)
        }
        // Right Cap
        drawUv(ctx, s, x + totalW - s.capSize, y, s.capSize, s.texH, s.texW - s.capSize, 0, s.capSize, s.texH)
    }

    private fun drawVertical(ctx: DrawContext, s: SeparatorStyle, x: Int, y: Int, totalH: Int) {
        val midTexH = s.texH - 2 * s.capSize
        val midH = totalH - 2 * s.capSize

        // Top Cap
        drawUv(ctx, s, x, y, s.texW, s.capSize, 0, 0, s.texW, s.capSize)
        // Middle (Stretched)
        if (midH > 0) {
            drawUv(ctx, s, x, y + s.capSize, s.texW, midH, 0, s.capSize, s.texW, midTexH)
        }
        // Bottom Cap
        drawUv(ctx, s, x, y + totalH - s.capSize, s.texW, s.capSize, 0, s.texH - s.capSize, s.texW, s.capSize)
    }

    private fun drawUv(ctx: DrawContext, s: SeparatorStyle, x: Int, y: Int, dW: Int, dH: Int, u: Int, v: Int, rW: Int, rH: Int) {
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, s.texture, x, y, u.toFloat(), v.toFloat(), dW, dH, rW, rH, s.texW, s.texH)
    }
}