# VFX / Particle API
Files: `src/main/java/com/myudog/myulib/api/VFXCompat.java`, `src/main/java/com/myudog/myulib/api/MyuVFXManager.java`, `src/main/java/com/myudog/myulib/api/dsl/EffectBuilder.java`, `src/main/java/com/myudog/myulib/api/floating/IFloatingObject.java`, `src/main/java/com/myudog/myulib/api/MyuVFX.java`
Overview
This area contains compatibility helpers, a small effect DSL, and prebuilt effect functions for server-world particle spawning and floating item objects. `VFXCompat.spawnParticles` uses reflection to call the best-matching `ServerLevel.spawnParticles` signature across mappings.
Public API
- `VFXCompat.spawnParticles(world, effect, x, y, z, count, offsetX, offsetY, offsetZ, speed)` — reflective invoker that adapts to various mappings.
- `EffectBuilder.spawnEffect(center, setup)` — Java effect DSL entry point for scheduling timed VFX work.
- `EffectBuilder.forCircle(radius, density, action)` — convenience helper for iterating circle points around the current effect center.
- `MyuVFXManager.spawnSpiral(world, center, particle)` — example effect that uses `VFXCompat` to spawn particles in a spiral over time.
- `MyuVFXManager.spawnShockwave(world, center, particle)` — example shockwave effect.
- `MyuVFX.createItemObject(world, itemStack)` — creates a floating item object wrapper.
Usage
Call `MyuVFXManager.spawnSpiral(ServerLevel, centerVec3, ParticleOptions)` from server code (e.g. commands or events) to spawn visual particle effects.
Notes
- `VFXCompat` throws `NoSuchMethodError` if no suitable `spawnParticles` method is found; ensure the environment includes at least one supported overload.
- `EffectBuilder` is intentionally lightweight and can be extended with more helpers as the Java API grows.
- For per-frame VFX that belong to a client-only system, consider implementing a client-side ParticleSystem instead of server spawn calls.