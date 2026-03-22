package com.myudog.myulib.api

import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import java.lang.reflect.Method

object VFXCompat {
    private val spawnMethods: List<Method> by lazy {
        val methods = mutableListOf<Method>()
        ServerWorld::class.java.declaredMethods.filter { it.name == "spawnParticles" }.forEach { methods += it }
        methods
    }

    /**
     * Try to invoke ServerWorld.spawnParticles with best-matching signature.
     * We accept the common parameters and dispatch reflectively to handle mapping differences.
     */
    fun spawnParticles(
        world: ServerWorld,
        effect: ParticleEffect,
        x: Double,
        y: Double,
        z: Double,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double,
        speed: Double
    ) {
        // Candidate signatures we support (param type arrays)
        val candidates = listOf(
            arrayOf(ParticleEffect::class.java, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Int::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType),
            arrayOf(ParticleEffect::class.java, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Double::class.javaPrimitiveType, Int::class.javaPrimitiveType)
        )

        for (m in spawnMethods) {
            val ptypes = m.parameterTypes
            for (cand in candidates) {
                if (ptypes.size == cand.size && ptypes.indices.all { ptypes[it] == cand[it] }) {
                    try {
                        m.isAccessible = true
                        if (cand[4] == Int::class.javaPrimitiveType) {
                            m.invoke(world, effect, x, y, z, count, offsetX, offsetY, offsetZ, speed)
                        } else {
                            // count is last
                            m.invoke(world, effect, x, y, z, offsetX, offsetY, offsetZ, speed, count)
                        }
                        return
                    } catch (_: Throwable) {
                        // try next
                    }
                }
            }
        }

        // If none matched, try to find a spawnParticles with 8 params (without count) and call multiple times
        val eight = spawnMethods.firstOrNull { it.parameterCount == 8 }
        if (eight != null) {
            try {
                eight.isAccessible = true
                // (effect,x,y,z,dx,dy,dz,speed) -> call with dx,dy,dz,speed and count times
                for (i in 0 until count) {
                    eight.invoke(world, effect, x, y, z, offsetX, offsetY, offsetZ, speed)
                }
                return
            } catch (_: Throwable) {}
        }

        // As last resort, throw
        throw NoSuchMethodError("No suitable spawnParticles overload found for this environment")
    }
}

