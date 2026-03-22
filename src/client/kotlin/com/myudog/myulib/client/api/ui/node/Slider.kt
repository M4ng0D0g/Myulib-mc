package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.component.*
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.gui.DrawContext

class Slider(
    val label: String? = null,
    val showValue: Boolean = true
) : LeafWidget() {

    private val data = SliderComponent()
    var isVertical = false

    // 材質 Key (對接 TextureRegistry)
    var trackTextureKey: String? = null
    var thumbTextureKey: String? = null

    init {
        // 水平預設 100x16，垂直預設 16x100
        transform.width = SizeUnit.Fixed(if (isVertical) 16f else 100f)
        transform.height = SizeUnit.Fixed(if (isVertical) 100f else 16f)

        MyulibApiClient.addComponent(entityId, data)

        // 註冊互動：點擊啟動拖拽
        MyulibApiClient.addComponent(entityId, ClickableComponent(
            onClick = { data.isDragging = true },
            onHover = { state.isHovered = it }
        ))
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()

        // 1. 繪製軌道 (Track)
        val trackPadding = 6
        if (isVertical) {
            context.fill(x + trackPadding, y, x + w - trackPadding, y + h, UiSchema.Action.BORDER_NORMAL.argb)
        } else {
            context.fill(x, y + trackPadding, x + w, y + h - trackPadding, UiSchema.Action.BORDER_NORMAL.argb)
        }

        // 2. 繪製進度填充 (Optional, 增加視覺效果)
        val fillAlpha = if (state.isHovered || data.isDragging) 0xFF else 0xAA
        val fillColor = UiSchema.Text.SUCCESS.withAlpha(fillAlpha).argb
        if (isVertical) {
            val fillH = (h * data.percentage).toInt()
            context.fill(x + trackPadding + 1, y + h - fillH, x + w - trackPadding - 1, y + h, fillColor)
        } else {
            val fillW = (w * data.percentage).toInt()
            context.fill(x, y + trackPadding + 1, x + fillW, y + h - trackPadding - 1, fillColor)
        }

        // 3. 繪製滑塊 (Thumb)
        val thumbSize = 8
        val thumbX = if (isVertical) x else x + ((w - thumbSize) * data.percentage).toInt()
        val thumbY = if (isVertical) y + ((h - thumbSize) * (1 - data.percentage)).toInt() else y

        val thumbColor = when {
            data.isDragging -> UiSchema.Action.BUTTON_HOVER.argb
            state.isHovered -> UiSchema.Action.BUTTON_BORDER.argb
            else -> UiSchema.Action.BUTTON_IDLE.argb
        }

        context.fill(thumbX, thumbY, thumbX + (if (isVertical) w else thumbSize), thumbY + (if (isVertical) thumbSize else h), thumbColor)
    }

    // --- Fluent API ---
    fun withRange(min: Double, max: Double) = apply { data.min = min; data.max = max }
    fun withValue(v: Double) = apply { data.value = v }
    fun withStep(s: Double) = apply { data.step = s }
    fun onValueChanged(handler: (Double) -> Unit) = apply { data.onValueChanged = { handler(data.value) } } // 重用之前的回調邏輯
}