package com.myudog.myulib.internal.generator

import com.myudog.myulib.api.shape.IShape
import net.minecraft.util.math.Vec3d
import kotlin.math.*

internal class TorusGenerator : IShape {
    override fun getOutlinePoints(size: Vec3d, density: Double): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        val majorR = size.x // 主半徑
        val minorR = size.y // 管半徑

        // 需要兩個角度來定義甜甜圈上的點
        val majorCount = (2 * PI * majorR * density).toInt()
        val minorCount = (2 * PI * minorR * density).toInt()

        for (i in 0 until majorCount) {
            val theta = 2 * PI * i / majorCount
            for (j in 0 until minorCount) {
                val phi = 2 * PI * j / minorCount

                // 甜甜圈參數方程
                val x = (majorR + minorR * cos(phi)) * cos(theta)
                val y = minorR * sin(phi)
                val z = (majorR + minorR * cos(phi)) * sin(theta)

                points.add(Vec3d(x, y, z))
            }
        }
        return points
    }

    override fun getSolidPoints(size: Vec3d, density: Double): List<Vec3d> {
        // 填充甜甜圈實心（透過多層管半徑）
        val points = mutableListOf<Vec3d>()
        var r = 0.1
        val rStep = 1.0 / density
        while (r <= size.y) { // 從中心到 Minor Radius
            points.addAll(getOutlinePoints(Vec3d(size.x, r, 0.0), density))
            r += rStep
        }
        return points
    }
}