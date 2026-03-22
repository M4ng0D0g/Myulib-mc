package com.myudog.myulib.api.color

import org.joml.Vector3f
import java.awt.Color

object ColorProvider {
    /** 16 進制轉 RGB (0.0 - 1.0) */
    fun hexToRGB(hex: String): Vector3f {
        val color = Color.decode(if (hex.startsWith("#")) hex else "#$hex")
        return Vector3f(color.red / 255f, color.green / 255f, color.blue / 255f)
    }

    /** 獲取兩個顏色間的線性漸層 */
    fun lerp(start: Vector3f, end: Vector3f, fraction: Float): Vector3f {
        return Vector3f(
            start.x + (end.x - start.x) * fraction,
            start.y + (end.y - start.y) * fraction,
            start.z + (end.z - start.z) * fraction
        )
    }

    /** HSL 轉換功能：可用於製作彩虹特效或動態亮度調整 */
    fun fromHSL(h: Float, s: Float, l: Float): Vector3f {
        val c = Color.getHSBColor(h, s, l) // HSB 在 Java 中與 HSL 相似
        return Vector3f(c.red / 255f, c.green / 255f, c.blue / 255f)
    }

    /**
     * 根據時間 (tick) 和週期 (period) 計算動態色相 (Rainbow Effect)
     * @param tick 當前 tick
     * @param period 完成一次 0~360度 色相循環所需的 tick 數
     * @param saturation 飽和度 (0.0 - 1.0)
     * @param brightness 亮度 (0.0 - 1.0)
     */
    fun getRainbowColor(tick: Int, period: Int, saturation: Float = 1.0f, brightness: Float = 1.0f): Vector3f {
        // 將 tick 轉換成 0.0 ~ 1.0 的週期進度
        val hue = (tick % period).toFloat() / period

        // 使用 Java 內建的 HSB 轉換 (HSB 與 HSL 相似，能滿足需求)
        val c = Color.getHSBColor(hue, saturation, brightness)
        return Vector3f(c.red / 255f, c.green / 255f, c.blue / 255f)
    }

    /**
     * 讓一個靜態顏色隨著時間「震盪」明度 (Pulstating Light)
     */
    fun pulsateBrightness(baseColor: Vector3f, tick: Int, period: Int, minLight: Float = 0.3f): Vector3f {
        val progress = (tick % period).toDouble() / period
        // 使用 Sine 波做出 0.0 ~ 1.0 ~ 0.0 的循環
        val multiplier = minLight + (1.0f - minLight) * (Math.sin(progress * 2 * Math.PI) * 0.5 + 0.5).toFloat()
        return Vector3f(baseColor.x * multiplier, baseColor.y * multiplier, baseColor.z * multiplier)
    }
}