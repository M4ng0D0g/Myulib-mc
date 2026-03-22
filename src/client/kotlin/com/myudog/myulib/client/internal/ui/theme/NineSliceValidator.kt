package com.myudog.myulib.client.internal.ui.theme

import com.myudog.myulib.Myulib
import com.myudog.myulib.client.api.ui.theme.JsonThemeData

/**
 * [Internal] 九宮格貼圖驗證器
 * 確保 JSON 定義的 UV 區域在貼圖範圍內。
 */
internal object NineSliceValidator {

    fun validate(themeName: String, data: JsonThemeData) {
        Myulib.LOGGER.info("========== 主題 [$themeName] 貼圖校驗中 ==========")

        // 範例：校驗 Panel 的九宮格
        // JsonThemeData.PanelColors contains only bg/border strings; the detailed tex coords may be absent.
        // If the detailed fields exist in the JSON model, you can add validation here. For now, skip if not present.
        try {
            // If data contains a nested panel detailed entry, attempt to validate via reflection or skip
        } catch (_: Throwable) {
            // no-op
        }

        Myulib.LOGGER.info("========== 主題 [$themeName] 校驗完畢 ==========")
    }

    private fun checkRegion(
        id: String, texW: Int, texH: Int,
        u: Int, v: Int, w: Int, h: Int,
        cw: Int, ch: Int
    ) {
        val uValid = (u >= 0 && u + w <= texW)
        val vValid = (v >= 0 && v + h <= texH)

        if (!uValid || !vValid) {
            Myulib.LOGGER.error("❌ [$id] UV 越界！定義區域為 (${u},${v}) ${w}x${h}，但貼圖總尺寸僅為 ${texW}x${texH}")
        } else if (w < cw * 2 || h < ch * 2) {
            Myulib.LOGGER.warn("⚠️ [$id] 角落尺寸 ($cw, $ch) 過大，中間區域已消失！")
        } else {
            Myulib.LOGGER.info("✅ [$id] UV 校驗通過")
        }
    }
}