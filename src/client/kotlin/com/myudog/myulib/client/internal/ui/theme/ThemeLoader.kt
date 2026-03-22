package com.myudog.myulib.client.internal.ui.theme

import com.google.gson.Gson
import com.myudog.myulib.client.api.ui.data.Color
import com.myudog.myulib.client.api.ui.theme.JsonThemeData
import com.myudog.myulib.client.api.ui.theme.UiTheme
import com.myudog.myulib.client.api.ui.data.UiSchema
import com.myudog.myulib.client.util.IdentifierCompat
import com.myudog.myulib.client.api.ui.theme.ThemeManager
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.io.InputStreamReader

internal object ThemeLoader {
    private val gson = Gson()

    /**
     * 從指定路徑載入 JSON 主題，並自動註冊所有樣式與配色
     */
    fun load(themeId: Identifier): UiTheme? {
        val resourcePath = IdentifierCompat.of(
            themeId.namespace,
            "myulib/themes/${themeId.path}.json"
        )

        return try {
            val resourceManager = MinecraftClient.getInstance().resourceManager
            val resource = resourceManager.getResource(resourcePath).orElse(null)

            resource?.let {
                val reader = InputStreamReader(it.inputStream)
                val data = gson.fromJson(reader, JsonThemeData::class.java)

                // 1. 執行九宮格與材質邊界校驗 [cite: 2026-03-22]
                NineSliceValidator.validate(themeId.toString(), data)

                // 2. 註冊動態物品槽樣式至管理器
                ThemeManager.registerSlotStyles(data.slots)

                // 3. 包裝並回傳主題實體
                wrapAsUiTheme(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 將 JSON 中的顏色字串 (#RRGGBB) 注入到全域 UiSchema 中
     */
    // No longer mutating UiSchema globally — wrap values into UiTheme

    private fun wrapAsUiTheme(data: JsonThemeData): UiTheme {
        return object : UiTheme {
            // 優先從 JSON 讀取面板配置，若無則提供 Fallback
            override val panelBg = Color.fromHex(data.panel.bg)
            override val panelBorder = Color.fromHex(data.panel.border)

            override val buttonIdle = Color.fromHex(data.button.idle)
            override val buttonHover = Color.fromHex(data.button.hover)
            override val buttonBorder = Color.fromHex(data.button.border)

            override val textPrimary = Color.fromHex(data.text.primary)
            override val textSecondary = Color.fromHex(data.text.secondary)

            override val textError = Color(0xFFFF4444.toInt())
            // JsonThemeData does not include a top-level 'colors' map; use panel.bg as a sensible fallback
            override val hpFill = try {
                Color.fromHex("#FF00FF88")
            } catch (_: Throwable) {
                Color(0xFFFF00FF.toInt())
            }
            override val hpGhost = Color(0xCCFFFFFF.toInt())
        }
    }
}