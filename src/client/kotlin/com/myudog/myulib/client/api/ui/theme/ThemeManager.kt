package com.myudog.myulib.client.api.ui.theme

import com.myudog.myulib.client.api.ui.data.SlotStyle
import com.myudog.myulib.client.internal.ui.theme.ThemeLoader
import net.minecraft.util.Identifier

object ThemeManager {

    private val slotStyleRegistry = mutableMapOf<String, SlotStyle>()

    // 預設回退樣式，防止路徑錯誤導致當機
    private val FALLBACK_STYLE = SlotStyle.DEFAULT

    // 預設主題 (Fallback)
    var current: UiTheme = DefaultDarkTheme
        private set

    val onThemeChangedListeners = mutableListOf<() -> Unit>()

    fun setTheme(theme: UiTheme) {
        current = theme
        onThemeChangedListeners.forEach { it.invoke() }
    }

    /**
     * 由 ThemeLoader 在讀取 JSON 時呼叫，將數據轉換為物件
     */
    fun registerSlotStyles(data: Map<String, JsonSlotStyleData>) {
        slotStyleRegistry.clear()
        data.forEach { (key, value) ->
            val style = SlotStyle(
                background = Identifier.of(value.background),
                border = value.border?.let { Identifier.of(it) },
                isSingleFile = value.isSingleFile
            )
            slotStyleRegistry[key] = style
        }
    }

    /**
     * [API] 獲取樣式，若找不到則回傳預設值
     */
    fun getSlotStyle(key: String): SlotStyle {
        return slotStyleRegistry.getOrDefault(key, FALLBACK_STYLE)
    }

    /**
     * 切換當前主題
     */
    fun applyTheme(themeId: Identifier) {
        val loaded = ThemeLoader.load(themeId)
        if (loaded != null) {
            current = loaded
            // 這裡可以發送事件通知 UI 系統刷新所有元件的快取
        }
    }
}