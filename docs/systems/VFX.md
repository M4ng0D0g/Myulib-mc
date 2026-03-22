# VFX / Particle API

Files: `src/main/kotlin/com/myudog/myulib/api/VFXCompat.kt`, `src/main/kotlin/com/myudog/myulib/api/MyuVFXManager.kt`

Overview

This area contains compatibility helpers and prebuilt effect functions for server-world particle spawning. `VFXCompat.spawnParticles` uses reflection to call the best-matching `ServerWorld.spawnParticles` signature across mappings.

Public API

- `VFXCompat.spawnParticles(world, effect, x, y, z, count, offsetX, offsetY, offsetZ, speed)` — reflective invoker that adapts to various mappings.
- `MyuVFXManager.spawnSpiral(world, center, particle)` — example effect that uses `VFXCompat` to spawn particles in a spiral over time.
- `MyuVFXManager.spawnShockwave(world, center, particle)` — example shockwave effect.

Usage

Call `MyuVFXManager.spawnSpiral(serverWorld, centerVec3d, particleEffect)` from server code (e.g. commands or events) to spawn visual particle effects.

Notes

- `VFXCompat` throws `NoSuchMethodError` if no suitable `spawnParticles` method is found; ensure the environment includes at least one supported overload.
- For per-frame VFX that belong to a client-only system, consider implementing a client-side ParticleSystem instead of server spawn calls.

