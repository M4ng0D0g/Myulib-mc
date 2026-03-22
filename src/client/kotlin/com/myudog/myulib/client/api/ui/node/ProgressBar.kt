package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.component.*
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.MinecraftClient
import com.myudog.myulib.client.util.Supplier
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

class ProgressBar : LeafWidget() {

    private val data = ProgressBarComponent()

    // 配置屬性
    var bgColor = UiSchema.Surface.PANEL.argb
    var fillColor = UiSchema.Text.SUCCESS.argb
    var ghostColor = UiSchema.Combat.HP_GHOST.argb
    var showText = false
    var textFormat = "%.0f%%"

    // 材質 Key (可選，套用你開發的 TextureRegistry)
    var bgTextureKey: String? = null
    var fillTextureKey: String? = null

    init {
        transform.width = SizeUnit.FillContainer
        transform.height = SizeUnit.Fixed(8f)
        MyulibApiClient.addComponent(entityId, data)
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        // 1. 更新進度數據 (動畫邏輯)
        updateProgress(delta)

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        // 2. 繪製背景
        context.fill(x, y, x + w, y + h, bgColor)

        // 3. 繪製殘影 (Ghost Bar) - 當顯示進度大於目標時出現 (如扣血)
        if (data.ghostProgress > data.currentProgress) {
            drawBar(context, x, y, w, h, data.ghostProgress, ghostColor)
        }

        // 4. 繪製主進度條
        drawBar(context, x, y, w, h, data.currentProgress, fillColor)

        // 5. 繪製文字
        if (showText) {
            val label = String.format(textFormat, data.targetProgress * 100)
            val tr = MinecraftClient.getInstance().textRenderer
            context.drawCenteredTextWithShadow(tr, Text.literal(label), x + w / 2, y + (h - 7) / 2, 0xFFFFFFFF.toInt())
        }
    }

    private fun updateProgress(delta: Float) {
        // 從供應器獲取最新數值
        data.progressSupplier?.let { data.targetProgress = it.get().coerceIn(0f, 1f) }

        // 平滑移動當前進度
        data.currentProgress = MathHelper.lerp(data.lerpSpeed * delta, data.currentProgress, data.targetProgress)

        // 殘影追趕邏輯：殘影會比主條慢一點，產生強烈的受擊感
        if (data.ghostProgress > data.targetProgress) {
            data.ghostProgress = MathHelper.lerp(data.lerpSpeed * 0.5f * delta, data.ghostProgress, data.targetProgress)
        } else {
            data.ghostProgress = data.targetProgress
        }
    }

    private fun drawBar(ctx: DrawContext, x: Int, y: Int, w: Int, h: Int, progress: Float, color: Int) {
        val barW = (w * progress).toInt()
        val barH = (h * progress).toInt()

        when (data.direction) {
            ProgressDirection.LEFT_TO_RIGHT -> ctx.fill(x, y, x + barW, y + h, color)
            ProgressDirection.RIGHT_TO_LEFT -> ctx.fill(x + w - barW, y, x + w, y + h, color)
            ProgressDirection.TOP_TO_BOTTOM -> ctx.fill(x, y, x + w, y + barH, color)
            ProgressDirection.BOTTOM_TO_TOP -> ctx.fill(x, y + h - barH, x + w, y + h, color)
        }
    }

    // --- Fluent API ---
    fun withProgress(p: Float) = apply { data.targetProgress = p; data.currentProgress = p; data.ghostProgress = p }
    fun withSupplier(s: () -> Float) = apply { data.progressSupplier = Supplier { s() } }
    fun withLerp(speed: Float) = apply { data.lerpSpeed = speed }
    fun withDirection(d: ProgressDirection) = apply { data.direction = d }
    fun showPercentage() = apply { showText = true }
}