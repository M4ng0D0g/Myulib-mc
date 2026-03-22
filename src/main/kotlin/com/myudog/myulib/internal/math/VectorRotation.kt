package com.myudog.myulib.internal.math

import net.minecraft.util.math.Vec3d
import kotlin.math.*

internal object VectorRotation {
    /**
     * 繞著指定的軸旋轉向量
     * @param vec 原向量
     * @param axis 旋轉軸 (例如 Vec3d(0, 1, 0) 是繞 Y 軸)
     * @param angle 弧度
     */
    fun rotate(vec: Vec3d, axis: Vec3d, angle: Double): Vec3d {
        val cosTheta = cos(angle)
        val sinTheta = sin(angle)
        val normalizedAxis = axis.normalize()

        // 羅德里格旋轉公式：V_rot = v*cosθ + (u x v)*sinθ + u*(u·v)*(1-cosθ)
        return vec.multiply(cosTheta)
            .add(normalizedAxis.crossProduct(vec).multiply(sinTheta))
            .add(normalizedAxis.multiply(normalizedAxis.dotProduct(vec) * (1 - cosTheta)))
    }
}