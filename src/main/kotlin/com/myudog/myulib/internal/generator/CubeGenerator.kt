package com.myudog.myulib.internal.generator

import com.myudog.myulib.api.shape.IShape
import net.minecraft.util.math.Vec3d

internal class CubeGenerator : IShape {
    override fun getOutlinePoints(size: Vec3d, density: Double): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        val half = size.multiply(0.5)
        val hx = half.x
        val hy = half.y
        val hz = half.z

        // 繪製 12 條邊
        fun drawLine(start: Vec3d, end: Vec3d) {
            val dist = start.distanceTo(end)
            val count = (dist * density).toInt()
            for (i in 0..count) {
                points.add(start.lerp(end, i.toDouble() / count))
            }
        }

        // 頂部與底部矩形邊框
        val corners = arrayOf(
            Vec3d(-hx, -hy, -hz), Vec3d(hx, -hy, -hz), Vec3d(hx, -hy, hz), Vec3d(-hx, -hy, hz),
            Vec3d(-hx, hy, -hz), Vec3d(hx, hy, -hz), Vec3d(hx, hy, hz), Vec3d(-hx, hy, hz)
        )

        for (i in 0..3) {
            drawLine(corners[i], corners[(i + 1) % 4]) // 底
            drawLine(corners[i + 4], corners[(i + 1) % 4 + 4]) // 頂
            drawLine(corners[i], corners[i + 4]) // 側邊柱
        }
        return points
    }

    override fun getSolidPoints(size: Vec3d, density: Double): List<Vec3d> {
        val points = mutableListOf<Vec3d>()
        val step = 1.0 / density
        var x = -size.x / 2
        while (x <= size.x / 2) {
            var y = -size.y / 2
            while (y <= size.y / 2) {
                var z = -size.z / 2
                while (z <= size.z / 2) {
                    points.add(Vec3d(x, y, z))
                    z += step
                }
                y += step
            }
            x += step
        }
        return points
    }
}