package com.myudog.myulib.internal.generator

import com.myudog.myulib.api.shape.IShape
import net.minecraft.util.math.Vec3d
import kotlin.math.*

internal class SphereGenerator : IShape {
    override fun getOutlinePoints(size: Vec3d, density: Double): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        val radius = size.x
        val count = (4 * PI * radius.pow(2) * density).toInt()

        for (i in 0 until count) {
            val phi = acos(1 - 2 * (i + 0.5) / count)
            val theta = PI * (1 + 5.0.pow(0.5)) * (i + 0.5)
            points.add(Vec3d(radius * sin(phi) * cos(theta), radius * cos(phi), radius * sin(phi) * sin(theta)))
        }
        return points
    }

    override fun getSolidPoints(size: Vec3d, density: Double): List<Vec3d> {
        // 使用分層半徑填充實心球
        val points = mutableListOf<Vec3d>()
        var r = 0.1
        while (r <= size.x) {
            points.addAll(getOutlinePoints(Vec3d(r, r, r), density))
            r += 1.0 / (density * 5) // 根據密度調整層距
        }
        return points
    }
}