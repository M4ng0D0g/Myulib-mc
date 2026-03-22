package com.myudog.myulib.api.`object`

import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

/**
 * 懸浮物體接口，支援位移、旋轉與縮放
 */
interface IFloatingObject {
    fun spawn(pos: Vec3d)
    fun remove()

    // 平滑變換 (Interpolation)
    fun moveTo(pos: Vec3d, interpolationDuration: Int)
    fun setScale(scale: Vector3f, interpolationDuration: Int)
    fun setRotation(leftRotation: Vector3f, interpolationDuration: Int)
}