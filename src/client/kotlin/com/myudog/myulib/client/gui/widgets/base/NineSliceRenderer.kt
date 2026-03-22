package com.myudog.myulib.client.gui.widgets.base

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import com.myudog.myulib.client.gui.enums.PanelStyle

/**
 * A small, functional nine-slice renderer used as a simplified implementation for the UI.
 *
 * It computes 9 segments (corners, edges, center) and issues drawTexture calls via DrawContext.
 * This is intentionally simple and focuses on correct drawing and type-safety rather than
 * advanced scaling modes or caching beyond the DrawParams structure.
 */
object NineSliceRenderer {
    // A single segment to draw: destination rect and source UV rect (in tex pixels)
    data class Segment(
        val dx: Int, val dy: Int, val dw: Int, val dh: Int,
        val u: Int, val v: Int, val sw: Int, val sh: Int
    )

    class DrawParams(val id: Identifier, val texW: Int, val texH: Int, val segments: List<Segment>)

    // Represent a simple single-texture nine-slice descriptor
    class SingleTextureNineSlice(
        val id: Identifier,
        val texW: Int,
        val texH: Int,
        val cornerW: Int,
        val cornerH: Int,
        val scaleMode: PanelStyle.ScaleMode
    ) {
        companion object {
            fun of(namespace: String, path: String, w: Int, h: Int, cornerW: Int, cornerH: Int, scaleMode: PanelStyle.ScaleMode): SingleTextureNineSlice? {
                return try {
                    SingleTextureNineSlice(com.myudog.myulib.client.util.IdentifierCompat.of(namespace, path), w, h, cornerW, cornerH, scaleMode)
                } catch (e: Throwable) {
                    // Fallback: still use IdentifierCompat to handle mapping differences
                    SingleTextureNineSlice(com.myudog.myulib.client.util.IdentifierCompat.of(namespace, path), w, h, cornerW, cornerH, scaleMode)
                }
            }
        }
    }

    // Helper to build a list of draw segments for the requested destination rect
    fun buildDrawParams(nine: SingleTextureNineSlice?, x: Int, y: Int, w: Int, h: Int): DrawParams {
        if (nine == null) throw IllegalArgumentException("nine slice descriptor required")

        val texW = nine.texW
        val texH = nine.texH
        // allow corners to shrink if target is smaller
        val left = minOf(nine.cornerW, w / 2)
        val right = minOf(nine.cornerW, w - left)
        val top = minOf(nine.cornerH, h / 2)
        val bottom = minOf(nine.cornerH, h - top)

        val midW = maxOf(0, w - left - right)
        val midH = maxOf(0, h - top - bottom)

        // source coordinates
        val uLeft = 0
        val uMid = nine.cornerW
        val uRight = texW - nine.cornerW

        val vTop = 0
        val vMid = nine.cornerH
        val vBottom = texH - nine.cornerH

        val segments = mutableListOf<Segment>()

        // Top row
        segments += Segment(x, y, left, top, uLeft, vTop, nine.cornerW, nine.cornerH)
        if (midW > 0) segments += Segment(x + left, y, midW, top, uMid, vTop, texW - 2 * nine.cornerW, nine.cornerH)
        segments += Segment(x + left + midW, y, right, top, uRight, vTop, nine.cornerW, nine.cornerH)

        // Middle row
        if (midH > 0) {
            segments += Segment(x, y + top, left, midH, uLeft, vMid, nine.cornerW, texH - 2 * nine.cornerH)
            if (midW > 0) segments += Segment(x + left, y + top, midW, midH, uMid, vMid, texW - 2 * nine.cornerW, texH - 2 * nine.cornerH)
            segments += Segment(x + left + midW, y + top, right, midH, uRight, vMid, nine.cornerW, texH - 2 * nine.cornerH)
        } else {
            // If no middle height, stretch top/bottom into middle area (degenerate)
            // we skip to keep behavior simple
        }

        // Bottom row
        segments += Segment(x, y + top + midH, left, bottom, uLeft, vBottom, nine.cornerW, nine.cornerH)
        if (midW > 0) segments += Segment(x + left, y + top + midH, midW, bottom, uMid, vBottom, texW - 2 * nine.cornerW, nine.cornerH)
        segments += Segment(x + left + midW, y + top + midH, right, bottom, uRight, vBottom, nine.cornerW, nine.cornerH)

        return DrawParams(nine.id, texW, texH, segments)
    }

    // Execute the prepared draw params using DrawContext
    fun executeDrawParams(ctx: DrawContext, nine: SingleTextureNineSlice?, params: DrawParams?) {
        if (params == null) return
        for (s in params.segments) {
            if (s.dw <= 0 || s.dh <= 0) continue
            ctx.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                params.id,
                s.dx, s.dy,
                s.u.toFloat(), s.v.toFloat(),
                s.dw, s.dh,
                s.sw, s.sh,
                params.texW, params.texH
            )
        }
    }

    // Direct draw using PanelStyle; style.singleTexture is expected to be SingleTextureNineSlice
    fun drawTextured(ctx: DrawContext, style: PanelStyle, x: Int, y: Int, w: Int, h: Int) {
        val nine = style.singleTexture as? SingleTextureNineSlice ?: return
        val params = buildDrawParams(nine, x, y, w, h)
        executeDrawParams(ctx, nine, params)
    }
}

// Use the central IdentifierCompat in com.myudog.myulib.client.util for mapping-safe construction.



