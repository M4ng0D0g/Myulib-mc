package com.myudog.myulib.client.api.ui.data

import net.minecraft.util.Identifier

/**
 * [API] 動態物品槽樣式數據。
 * @param background 背景貼圖路徑
 * @param border 邊框貼圖路徑 (可選)
 * @param isSingleFile 是否為單一檔案 (若是，則忽略邊框層)
 */
data class SlotStyle(
    val background: Identifier,
    val border: Identifier? = null,
    val isSingleFile: Boolean = false
) {
    companion object {
        // 提供一組預設樣式，方便快速開發
        val DEFAULT = SlotStyle(
            Identifier.of("myulib", "textures/gui/slot_bg_default.png"),
            Identifier.of("myulib", "textures/gui/slot_bd_default.png")
        )
        val INVENTORY = SlotStyle(Identifier.of("myulib", "textures/gui/inventory_default.png"), isSingleFile = true)
    }
}