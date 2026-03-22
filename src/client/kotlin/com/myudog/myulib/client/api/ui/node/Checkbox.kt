package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.component.CheckboxComponent
import com.myudog.myulib.client.api.ui.component.ClickableComponent
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

/**
 * [API] 核取方塊元件。
 * 支援文字標籤與自動測量寬度。
 */
class Checkbox(
    var label: String,
    initialChecked: Boolean = false
) : LeafWidget() {

    private val data = CheckboxComponent(initialChecked)
    var labelColor: Int = UiSchema.Text.PRIMARY.argb

    // 樣式 Key (可選，若不設定則使用 UiSchema 顏色繪製)
    var boxTextureKey: String? = null
    var checkTextureKey: String? = null

    init {
        // Checkbox 通常高度固定為 12px，寬度隨文字長度變化
        transform.width = SizeUnit.WrapContent
        transform.height = SizeUnit.Fixed(12f)

        MyulibApiClient.addComponent(entityId, data)

        // 註冊互動：點擊即切換狀態
        MyulibApiClient.addComponent(entityId, ClickableComponent(
            onClick = {
                if (state.isEnabled) {
                    data.isChecked = !data.isChecked
                    data.onToggle(data.isChecked)
                    // 播放點擊音效
                    MinecraftClient.getInstance().soundManager.play(
                        net.minecraft.client.sound.PositionedSoundInstance.master(
                            net.minecraft.sound.SoundEvents.UI_BUTTON_CLICK, 1.2f
                        )
                    )
                }
            },
            onHover = { state.isHovered = it }
        ))
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val textRenderer = MinecraftClient.getInstance().textRenderer

        // 1. 繪製方框 (Box)
        val borderColor = if (state.isHovered) UiSchema.Action.BUTTON_BORDER.argb else UiSchema.Action.BORDER_NORMAL.argb
        val bgColor = if (data.isChecked) UiSchema.Text.SUCCESS.withAlpha(0x80).argb else UiSchema.Action.BUTTON_IDLE.argb

        // 繪製背景與邊框 (此處可對接 TextureRegistry，這裡示範純色繪製)
        context.fill(x, y, x + 12, y + 12, borderColor)
        context.fill(x + 1, y + 1, x + 11, y + 11, bgColor)

        // 2. 繪製勾選符號
        if (data.isChecked) {
            context.drawText(textRenderer, "✓", x + 2, y + 2, UiSchema.Text.PRIMARY.argb, false)
        }

        // 3. 繪製標籤 (Label)
        if (label.isNotEmpty()) {
            val color = if (state.isEnabled) labelColor else UiSchema.Text.DISABLED.argb
            context.drawText(textRenderer, label, x + 16, y + 2, color, false)
        }
    }

    // --- Fluent API ---
    fun onToggle(handler: (Boolean) -> Unit) = apply { data.onToggle = handler }
    fun withLabelColor(color: Int) = apply { this.labelColor = color }
    fun setChecked(checked: Boolean) { data.isChecked = checked }
    fun isChecked(): Boolean = data.isChecked
}