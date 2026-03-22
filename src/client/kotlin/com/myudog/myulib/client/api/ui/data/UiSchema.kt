package com.myudog.myulib.client.api.ui.data

/**
 * [API] 全域色彩 Schema。
 * 仿 CSS 變數結構，將色彩依據功能分組。
 */
object UiSchema {

    // ── 基礎色盤 (Palette) ──────────────────────────────────────────
    object Palette {
        val WHITE      = Color(0xFFFFFFFF.toInt())
        val GRAY_100   = Color(0xFFE0E0E0.toInt())
        val GRAY_500   = Color(0xFF999999.toInt())
        val GRAY_900   = Color(0xFF191919.toInt())
        val RED        = Color(0xFF993333.toInt())
        val BLUE       = Color(0xFF334CB2.toInt())
        val PURPLE     = Color(0xFF7F3FB2.toInt())
        val DARK_BLUE  = Color(0xFF181830.toInt())
    }

    // ── 介面背景 (Surface) ──────────────────────────────────────────
    object Surface {
        val PANEL       = Palette.DARK_BLUE.withAlpha(0xE0)
        val TOOLTIP     = Palette.GRAY_900.withAlpha(0xF0)
        val OVERLAY     = Color(0x80000000.toInt())
    }

    // ── 文字色彩 (Text) ──────────────────────────────────────────────
    object Text {
        val PRIMARY     = Palette.GRAY_100
        val SECONDARY   = Palette.GRAY_500
        val ERROR       = Palette.RED
        val SUCCESS     = Color(0xFF00FF00.toInt())
        val WARNING     = Color(0xFFFFAA00.toInt())
        val DISABLED    = Color(0xFF7F7F7F.toInt())
    }

    // ── 互動元件 (Action) ────────────────────────────────────────────
    object Action {
        val BUTTON_IDLE    = Color(0xFF303050.toInt())
        val BUTTON_HOVER   = Color(0xFF404070.toInt())
        val BUTTON_BORDER  = Color(0xFF5050A0.toInt())
        val BORDER_NORMAL  = Color(0xFF3A3A5A.toInt())
        val BORDER_DARK    = Color(0xFF242424.toInt())
        val BUTTON_ACTIVE  = Color(0xFF6060B0.toInt())
    }

    // ── 戰鬥數值 (Combat) ────────────────────────────────────────────
    object Combat {
        val HP_GHOST       = Color(0xCCFFFFFF.toInt())
        val HP_POISON      = Color(0xCC8800CC.toInt())
        val SHIELD         = Color(0x88AADDFF.toInt())
    }

    // Placeholder color used when a texture is missing and debugMode is enabled
    val ICON_PLACEHOLDER = Color(0x44FF00FF.toInt())
}