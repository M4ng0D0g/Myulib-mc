package com.myudog.myulib.internal.generator

import com.myudog.myulib.api.shape.IShape
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import kotlin.math.max

/**
 * Simple circle generator implementing IShape.
 * Produces points in the XZ plane (y = 0) centered at origin.
 */
class CircleGenerator : IShape {
    override fun getOutlinePoints(size: Vec3d, density: Double): List<Vec3d> {
        val radius = size.x
        val count = max(3, density.toInt())
        val points = ArrayList<Vec3d>(count)
        for (i in 0 until count) {
            val angle = 2.0 * PI * i / count
            val x = radius * cos(angle)
            val z = radius * sin(angle)
            points.add(Vec3d(x, 0.0, z))
        }
        return points
    }

    override fun getSolidPoints(size: Vec3d, density: Double): List<Vec3d> {
        val radius = size.x
        val rings = max(1, (density / 2).toInt())
        val segments = max(6, density.toInt())
        val points = ArrayList<Vec3d>()
        for (r in 0..rings) {
            val rr = radius * (r.toDouble() / rings)
            for (i in 0 until segments) {
                val angle = 2.0 * PI * i / segments
                val x = rr * cos(angle)
                val z = rr * sin(angle)
                points.add(Vec3d(x, 0.0, z))
            }
        }
        return points
    }
}

/** Convenience extension used by older DSLs expecting calculatePoints(radius, density:Int) */
fun IShape.calculatePoints(radius: Double, density: Int): List<Vec3d> {
    return this.getOutlinePoints(Vec3d(radius, 0.0, radius), density.toDouble())
}

