package com.myudog.myulib.internal.dynamics

import com.myudog.myulib.api.dynamics.IForceField
import net.minecraft.util.math.Vec3d

internal class VortexForceField(val axis: Vec3d = Vec3d(0.0, 1.0, 0.0)) : IForceField {
    override fun calculateForce(pos: Vec3d, center: Vec3d, strength: Double): Vec3d {
        // 1. 取得從中心指向粒子的向量
        val relativePos = pos.subtract(center)
        if (relativePos.lengthSquared() < 0.001) return Vec3d.ZERO

        // 2. 計算切向向量 (外積)
        // 旋轉方向取決於軸的方向與外積順序
        val tangent = relativePos.crossProduct(axis).normalize()

        // 3. 渦流通常隨距離增加而減弱，或是保持恆定
        return tangent.multiply(strength)
    }
}