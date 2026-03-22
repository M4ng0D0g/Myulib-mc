package com.myudog.myulib.client.api.ui.theme

/**
 * [Internal] 對接 JSON 的原始數據格式
 */
data class JsonThemeData(
    val panel: PanelColors = PanelColors(),
    val button: ButtonColors = ButtonColors(),
    val slots: Map<String, JsonSlotStyleData>, // ✨ 新增：多樣化的物品槽樣式
    val text: TextColors = TextColors()
) {
    data class PanelColors(val bg: String = "#E0181830", val border: String = "#FF3A3A5A")
    data class ButtonColors(val idle: String = "#FF303050", val hover: String = "#FF404070", val border: String = "#FF5050A0")
    data class TextColors(val primary: String = "#FFE0E0E0", val secondary: String = "#FFAAAAAA")
}

data class JsonSlotStyleData(
    val background: String,
    val border: String? = null,
    val isSingleFile: Boolean = false
)