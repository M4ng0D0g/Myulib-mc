package com.myudog.myulib.client.api.ui.theme

import com.myudog.myulib.client.api.ui.data.Color

/**
 * [API] 主題規格定義。
 * 未來如果你想做「深色模式」或「高對比模式」，只需實作此接口。
 */
interface UiTheme {
    // 面板相關
    val panelBg: Color
    val panelBorder: Color

    // 按鈕相關
    val buttonIdle: Color
    val buttonHover: Color
    val buttonBorder: Color

    // 文字相關
    val textPrimary: Color
    val textSecondary: Color
    val textError: Color

    // 狀態條相關
    val hpFill: Color
    val hpGhost: Color
}

/**
 * [實作] 預設風格 (繼承自你舊版的顏色組合)
 */
object DefaultDarkTheme : UiTheme {
    override val panelBg = Color(0xE0181830.toInt())
    override val panelBorder = Color(0xFF3A3A5A.toInt())

    override val buttonIdle = Color(0xFF303050.toInt())
    override val buttonHover = Color(0xFF404070.toInt())
    override val buttonBorder = Color(0xFF5050A0.toInt())

    override val textPrimary = Color(0xFFE0E0E0.toInt())
    override val textSecondary = Color(0xFFAAAAAA.toInt())
    override val textError = Color(0xFFFF4444.toInt())

    override val hpFill = Color(0xFF00FF88.toInt())
    override val hpGhost = Color(0xCCFFFFFF.toInt())
}