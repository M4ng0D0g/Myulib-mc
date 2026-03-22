package com.myudog.myulib.internal.generator

import com.myudog.myulib.api.shape.IShape
import net.minecraft.util.math.Vec3d
import kotlin.math.*

internal class CylinderGenerator : IShape {
    override fun getOutlinePoints(size: Vec3d, density: Double): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        val radiusX = size.x
        val radiusZ = size.z
        val height = size.y

        // 1. 生成上下兩個圓盤的邊框
        val circlePoints = (radiusX + radiusZ) * PI * density // 粗略估算點數
        val step = 2 * PI / circlePoints

        for (i in 0 until circlePoints.toInt()) {
            val angle = i * step
            val x = radiusX * cos(angle)
            val z = radiusZ * sin(angle)
            points.add(Vec3d(x, 0.0, z)) // 底部
            points.add(Vec3d(x, height, z)) // 頂部
        }

        // 2. 生成側面的垂直線線 (以密度為準)
        for (i in 0 until (height * density).toInt()) {
            val h = i / density
            // 在側面均勻選四個點連線 (或更多)
            for (j in 0..3) {
                val angle = j * PI / 2
                points.add(Vec3d(radiusX * cos(angle), h, radiusZ * sin(angle)))
            }
        }
        return points
    }

    override fun getSolidPoints(size: Vec3d, density: Double): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        val height = size.y
        // 均勻填充圓柱 (分層填充)
        var h = 0.0
        val hStep = 1.0 / density
        while (h <= height) {
            // 每一層使用 CircleGenerator 的 getSolidPoints
            points.addAll(com.myudog.myulib.api.Shapes.CIRCLE.getSolidPoints(Vec3d(size.x, 0.0, size.z), density))
            points.replaceAll { if (it.y == 0.0) Vec3d(it.x, h, it.z) else it } // 修正高度
            h += hStep
        }
        return points
    }
}