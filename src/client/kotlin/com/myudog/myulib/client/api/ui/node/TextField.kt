package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.component.*
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

class TextField(width: Float = 120f) : LeafWidget() {

    val data = TextFieldComponent()
    var textureKey: String? = null // 可對接 TextureRegistry
    private var tickCounter = 0

    init {
        transform.width = SizeUnit.Fixed(width)
        transform.height = SizeUnit.Fixed(16f)

        MyulibApiClient.addComponent(entityId, data)

        // 註冊點擊以獲取焦點
        MyulibApiClient.addComponent(entityId, ClickableComponent(
            onClick = { state.isFocused = true }
        ))
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return
        tickCounter++

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val w = computed.w.toInt()
        val h = computed.h.toInt()
        val tr = MinecraftClient.getInstance().textRenderer

        // 1. 繪製背景與邊框 (使用 UiSchema)
        val bg = if (state.isFocused) UiSchema.Action.BUTTON_HOVER.argb else UiSchema.Action.BUTTON_IDLE.argb
        val border = if (state.isFocused) UiSchema.Action.BUTTON_BORDER.argb else UiSchema.Action.BORDER_NORMAL.argb
        drawBox(context, x, y, w, h, bg, border)

        // 2. 繪製內容或預留字
        val isNotEmpty = data.text.isNotEmpty()
        val drawText = if (isNotEmpty) data.text else data.placeholder
        val drawColor = if (isNotEmpty) UiSchema.Text.PRIMARY.argb else UiSchema.Text.SECONDARY.argb

        // 這裡可以加入文字滾動偏移計算 (Truncate 邏輯)
        context.drawText(tr, Text.literal(drawText), x + 4, y + (h - 8) / 2, drawColor, false)

        // 3. 繪製閃爍游標
        if (state.isFocused && (tickCounter / 10) % 2 == 0) {
            val beforeCursor = data.text.substring(0, data.cursorPos.coerceAtMost(data.text.length))
            val cursorX = x + 4 + tr.getWidth(beforeCursor)
            if (cursorX < x + w - 4) {
                context.fill(cursorX, y + 3, cursorX + 1, y + h - 3, UiSchema.Text.PRIMARY.argb)
            }
        }
    }

    private fun drawBox(ctx: DrawContext, x: Int, y: Int, w: Int, h: Int, bg: Int, border: Int) {
        ctx.fill(x, y, x + w, y + h, border)
        ctx.fill(x + 1, y + 1, x + w - 1, y + h - 1, bg)
    }

    // --- Fluent API ---
    fun withPlaceholder(text: String) = apply { data.placeholder = text }
    fun withFilter(filter: (Char) -> Boolean) = apply { data.charFilter = filter }
    fun onTextChanged(handler: (String) -> Unit) = apply { data.onTextChanged = handler }
}