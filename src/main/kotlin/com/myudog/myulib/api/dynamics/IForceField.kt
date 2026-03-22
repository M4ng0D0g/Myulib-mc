package com.myudog.myulib.api.dynamics

import net.minecraft.util.math.Vec3d

interface IForceField {
    /** 計算特定座標受到的力向量 */
    fun calculateForce(pos: Vec3d, center: Vec3d, strength: Double): Vec3d
}