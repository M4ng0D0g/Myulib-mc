package com.myudog.myulib.internal.dynamics

import com.myudog.myulib.api.dynamics.IForceField
import net.minecraft.util.math.Vec3d
import kotlin.math.max

internal class RadialForceField(
    private val isAttractive: Boolean, // true 為引力，false 為排斥力
    private val maxRange: Double = 10.0 // 影響範圍，超過此距離不予計算
) : IForceField {

    override fun calculateForce(pos: Vec3d, center: Vec3d, strength: Double): Vec3d {
        // 1. 計算方向向量
        val direction = if (isAttractive) center.subtract(pos) else pos.subtract(center)
        val distanceSq = direction.lengthSquared()

        // 2. 距離檢查：優化效能，超出範圍直接回傳零向量
        if (distanceSq > maxRange * maxRange || distanceSq < 0.0001) {
            return Vec3d.ZERO
        }

        val distance = Math.sqrt(distanceSq)

        // 3. 調整力場衰減模型
        // 建議在 VFX 中使用 1/r (線性衰減) 或 (1 - d/R) (邊緣消失)，這比 1/r^2 更容易控制視覺效果
        // 這裡採用線性衰減公式，並加上 strength 控制
        val falloff = (maxRange - distance) / maxRange
        val magnitude = strength * falloff

        // 4. 回傳標準化後的力向量
        return direction.normalize().multiply(magnitude)
    }
}