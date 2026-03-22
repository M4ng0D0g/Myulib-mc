package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * [API] 提示框顯示節點。
 * 自動處理螢幕邊界，防止提示框跑出畫面。
 */
class Tooltip : Box() {
    init {
        // Tooltip 預設樣式
        textureKey = "tooltip_bg"
        flexContainer.spacing = 2f
        itemConfig.padding.set(4f)
        transform.width = SizeUnit.WrapContent
        transform.height = SizeUnit.WrapContent
    }

    /**
     * 更新座標並進行螢幕邊界修正
     */
    fun updatePosition(mouseX: Int, mouseY: Int, screenW: Int, screenH: Int) {
        val w = computed.w
        val h = computed.h

        // 預設在滑鼠右下方偏移
        var targetX = mouseX + 12f
        var targetY = mouseY + 12f

        // 右邊界檢查
        if (targetX + w > screenW) targetX = mouseX - w - 4f
        // 下邊界檢查
        if (targetY + h > screenH) targetY = mouseY - h - 4f

        transform.offsetX = targetX
        transform.offsetY = targetY
    }
}