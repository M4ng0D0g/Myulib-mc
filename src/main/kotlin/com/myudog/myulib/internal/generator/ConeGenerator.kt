package com.myudog.myulib.internal.generator

import com.myudog.myulib.api.shape.IShape
import net.minecraft.util.math.Vec3d
import kotlin.math.*

internal class ConeGenerator : IShape {
    override fun getOutlinePoints(size: Vec3d, density: Double): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        val radiusX = size.x
        val radiusZ = size.z
        val height = size.y

        // 1. 底圓邊框
        val circleCount = (2 * PI * max(radiusX, radiusZ) * density).toInt()
        for (i in 0 until circleCount) {
            val angle = 2 * PI * i / circleCount
            points.add(Vec3d(radiusX * cos(angle), 0.0, radiusZ * sin(angle)))
        }

        // 2. 側面斜線 (從頂點到底圓選 8 條斜線作為骨架)
        for (i in 0 until 8) {
            val angle = 2 * PI * i / 8
            val basePos = Vec3d(radiusX * cos(angle), 0.0, radiusZ * sin(angle))
            val tipPos = Vec3d(0.0, height, 0.0)
            val lineDensity = (height * density).toInt()
            for (j in 0..lineDensity) {
                points.add(basePos.lerp(tipPos, j.toDouble() / lineDensity))
            }
        }
        return points
    }

    override fun getSolidPoints(size: Vec3d, density: Double): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        val hStep = 1.0 / density
        var h = 0.0
        while (h <= size.y) {
            // 隨高度增加，圓盤半徑線性遞減 (r = R * (1 - h/H))
            val ratio = 1.0 - (h / size.y)
            val currentRadius = Vec3d(size.x * ratio, 0.0, size.z * ratio)

            // 複用 Circle 的實心計算
            com.myudog.myulib.api.Shapes.CIRCLE.getSolidPoints(currentRadius, density).forEach {
                points.add(Vec3d(it.x, h, it.z))
            }
            h += hStep
        }
        return points
    }
}