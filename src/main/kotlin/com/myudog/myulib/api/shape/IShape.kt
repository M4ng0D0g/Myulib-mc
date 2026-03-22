package com.myudog.myulib.api.shape

import net.minecraft.util.math.Vec3d

interface IShape {
    /** 獲取邊框點 */
    fun getOutlinePoints(size: Vec3d, density: Double): List<Vec3d>

    /** 獲取內部填充點 (實心) */
    fun getSolidPoints(size: Vec3d, density: Double): List<Vec3d>
}