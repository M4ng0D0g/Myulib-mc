package com.myudog.myulib.api

import com.myudog.myulib.api.dsl.EffectBuilder
import com.myudog.myulib.api.dsl.spawnEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin

object MyuVFXManager {

    /**
     * 預設特效：螺旋升天 (SpiralUp)
     */
    fun spawnSpiral(world: ServerWorld, center: Vec3d, particle: ParticleEffect) {
        spawnEffect(center) {
            duration(40)
            onTick { tick ->
                val angle = tick * 0.5
                val radius = 1.5
                val x = radius * cos(angle)
                val z = radius * sin(angle)
                val y = tick * 0.1

                // offsets (dx, dy, dz, speed) then count last to match various mappings
                VFXCompat.spawnParticles(world, particle, center.x + x, center.y + y, center.z + z, 1, 0.0, 0.0, 0.0, 0.0)
            }
        }
    }

    /**
     * 預設特效：衝擊波 (Shockwave)
     */
    fun spawnShockwave(world: ServerWorld, center: Vec3d, particle: ParticleEffect) {
        spawnEffect(center) {
            duration(15)
            onTick { tick ->
                val radius = tick * 0.8
                val density = (radius * 10.0).toInt().coerceAtLeast(8).toDouble()
                forCircle(radius, density) { pos ->
                    VFXCompat.spawnParticles(world, particle, pos.x, pos.y, pos.z, 1, 0.0, 0.1, 0.0, 0.02)
                }
            }
        }
    }
}