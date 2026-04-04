package com.myudog.myulib.client.api.ui.theme

import com.myudog.myulib.client.api.ui.data.Color

/**
 * [API] 主題規格定義。
 * 未來如果你想做「深色模式」或「高對比模式」，只需實作此接口。
 */
interface UiTheme {
    val name: String

    // --- Semantic Colors ---
    val primary: Color
    val primaryHover: Color
    val primaryActive: Color

    val secondary: Color
    val secondaryHover: Color
    val secondaryActive: Color

    val background: Color
    val surface: Color
    val surfaceHighlight: Color

    // 面板相關 (保留相容或替換)
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

    // --- Spacing & Formatting (CSS-like) ---
    val paddingSmall: Int
    val paddingBase: Int
    val paddingLarge: Int

    val gapSmall: Int
    val gapBase: Int
    val gapLarge: Int

    val borderWidth: Int
    val borderRadius: Int

    val fontSizeSmall: Float
    val fontSizeBase: Float
    val fontSizeLarge: Float
}

/**
 * [實作] 預設風格 (繼承自你舊版的顏色組合)，具備開發擴充性
 */
open class DefaultTheme(
    override val name: String = "Default Dark"
) : UiTheme {
    override val primary = Color(0xFF5B6EE1.toInt())
    override val primaryHover = Color(0xFF639BFF.toInt())
    override val primaryActive = Color(0xFF3F51B5.toInt())

    override val secondary = Color(0xFF696A6A.toInt())
    override val secondaryHover = Color(0xFF8B9BB4.toInt())
    override val secondaryActive = Color(0xFF595652.toInt())

    override val background = Color(0xFF1E1E1E.toInt())
    override val surface = Color(0xFF2D2D30.toInt())
    override val surfaceHighlight = Color(0xFF3E3E42.toInt())

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

    override val paddingSmall = 2
    override val paddingBase = 4
    override val paddingLarge = 8

    override val gapSmall = 2
    override val gapBase = 4
    override val gapLarge = 8

    override val borderWidth = 1
    override val borderRadius = 4

    override val fontSizeSmall = 0.8f
    override val fontSizeBase = 1.0f
    override val fontSizeLarge = 1.5f
}

object DefaultDarkTheme : DefaultTheme("Default Dark")

object DefaultLightTheme : DefaultTheme("Default Light") {
    override val primary = Color(0xFF3F51B5.toInt())
    override val primaryHover = Color(0xFF5B6EE1.toInt())
    override val primaryActive = Color(0xFF303F9F.toInt())

    override val secondary = Color(0xFFB0B0B0.toInt())
    override val secondaryHover = Color(0xFFCCCCCC.toInt())
    override val secondaryActive = Color(0xFF909090.toInt())

    override val background = Color(0xFFF0F0F0.toInt())
    override val surface = Color(0xFFFFFFFF.toInt())
    override val surfaceHighlight = Color(0xFFE0E0E0.toInt())

    override val panelBg = Color(0xE0F0F0F0.toInt())
    override val panelBorder = Color(0xFFCCCCCC.toInt())

    override val buttonIdle = Color(0xFFE0E0E0.toInt())
    override val buttonHover = Color(0xFFF5F5F5.toInt())
    override val buttonBorder = Color(0xFFB0B0B0.toInt())

    override val textPrimary = Color(0xFF202020.toInt())
    override val textSecondary = Color(0xFF505050.toInt())
}