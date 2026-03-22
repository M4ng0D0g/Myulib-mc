package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.entity.LivingEntity
import net.minecraft.text.Text
import java.util.function.Supplier
import kotlin.math.min

/**
 * [API] 3D 實體紙娃娃元件。
 * 支援縮放、滑鼠追蹤以及動態實體供應。
 */
class EntityPaperdoll(
    var entity: LivingEntity? = null,
    var entitySupplier: Supplier<out LivingEntity>? = null
) : LeafWidget() {

    var scaleFactor: Float = 0.5f // 相對於元件大小的縮放比例

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val targetEntity = entitySupplier?.get() ?: entity
        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        if (targetEntity != null) {
            // 計算 3D 渲染區域與比例
            // $Scale = \max(8, \min(W, H) \times Factor)$
            val scale = (min(w, h) * scaleFactor).toInt().coerceAtLeast(8)

            // 繪製位置計算 (居中投影)
            val x1 = x; val y1 = y
            val x2 = x + w; val y2 = y + h

            try {
                // 使用 Minecraft 原生實體渲染接口
                InventoryScreen.drawEntity(context, x1, y1, x2, y2, scale, 0.0625f, mouseX.toFloat(), mouseY.toFloat(), targetEntity)
                return
            } catch (e: Exception) {
                // 渲染失敗時降級顯示
            }
        }

        // 渲染佔位符 (?)
        drawPlaceholder(context, x, y, w, h)
    }

    private fun drawPlaceholder(context: DrawContext, x: Int, y: Int, w: Int, h: Int) {
        val tr = MinecraftClient.getInstance().textRenderer
        context.fill(x + 2, y + 2, x + w - 2, y + h - 2, 0x40202020)
        val text = Text.literal("?")
        context.drawText(tr, text, x + (w - tr.getWidth(text)) / 2, y + (h - 8) / 2, 0xFFFFFFFF.toInt(), false)
    }
}