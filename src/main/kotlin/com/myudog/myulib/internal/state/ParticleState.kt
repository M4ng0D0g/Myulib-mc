package com.myudog.myulib.internal.state

import net.minecraft.util.math.Vec3d

internal data class ParticleState(
    var pos: Vec3d,           // 相對座標
    var vel: Vec3d,           // 速度向量
    var age: Int = 0,         // 當前歲數 (ticks)
    val maxAge: Int,          // 最大壽命
    val randomSeed: Double = Math.random() // 用於個別粒子的隨機行為
) {
    val progress: Float get() = age.toFloat() / maxAge // 生命進度 (0.0 ~ 1.0)
    val isDead: Boolean get() = age >= maxAge
}