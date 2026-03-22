package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.theme.ThemeManager
import com.myudog.myulib.client.api.ui.data.SlotStyle
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import com.myudog.myulib.client.util.drawItemInGuiWithOverridesCompat

/**
 * [API] 物品槽元件。
 * 支援透過 styleKey 動態切換主題貼圖，並處理物品渲染與佔位符邏輯。
 */
open class ItemSlot(var stack: ItemStack = ItemStack.EMPTY) : LeafWidget() {

    // 改為儲存 Key，達成與具體主題數據的解耦
    var styleKey: String = "default"

    // 支援空槽位的裝飾圖示 (例如頭盔、劍的灰色剪影)
    var placeholderIcon: Identifier? = null

    init {
        // 標準物品槽為 18x18 像素
        transform.width = SizeUnit.Fixed(18f)
        transform.height = SizeUnit.Fixed(18f)
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        val x = computed.x.toInt()
        val y = computed.y.toInt()
        val client = MinecraftClient.getInstance()
        val textRenderer = client.textRenderer

        // 1. 動態取得當前主題的材質樣式
        val style = ThemeManager.getSlotStyle(styleKey)

        // 2. 繪製材質背景 (底層 -> 邊框層)
        renderSlotTexture(context, style, x, y)

        // 3. 根據物品狀態渲染內容
        if (stack.isEmpty) {
            // 情況 A：渲染半透明佔位圖示
            placeholderIcon?.let { icon ->
                // Best-effort semi-transparent placeholder: draw texture with RenderPipelines overload
                context.drawTexture(RenderPipelines.GUI_TEXTURED, icon, x + 1, y + 1, 0f, 0f, 16, 16, 16, 16)
            }
        } else {
            // 情況 B：渲染物品實體與疊加層 (數量、耐久度)
            context.drawItem(stack, x + 1, y + 1)
            // Use compatibility wrapper that will reflectively call the correct overlay method if present
            context.drawItemInGuiWithOverridesCompat(textRenderer, stack, x + 1, y + 1)
        }

        // 4. 繪製懸停高亮遮罩
        if (state.isHovered) {
            // 保持 SlotRenderer 時代的經典高亮感
            context.fill(x + 1, y + 1, x + 17, y + 17, 0x60FFFFFF)
        }
    }

    /**
     * [內部] 處理多層材質渲染邏輯
     */
    private fun renderSlotTexture(context: DrawContext, style: SlotStyle, x: Int, y: Int) {
        // 第一層：背景 (必選)
        context.drawTexture(RenderPipelines.GUI_TEXTURED, style.background, x, y, 0f, 0f, 18, 18, 18, 18)

        // 第二層：邊框 (如果樣式非單一檔案且存在邊框路徑)
        if (!style.isSingleFile && style.border != null) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, style.border, x, y, 0f, 0f, 18, 18, 18, 18)
        }
    }

    // ── 流式 API (Fluent API) ───────────────────────────────────────────

    /** 設定樣式 Key，對應 theme.json 中的定義 */
    fun withStyle(key: String): ItemSlot {
        this.styleKey = key
        return this
    }

    fun withPlaceholder(icon: Identifier): ItemSlot {
        this.placeholderIcon = icon
        return this
    }
}