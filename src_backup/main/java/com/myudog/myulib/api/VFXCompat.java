package com.myudog.myulib.api;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VFXCompat {
    @FunctionalInterface
    private interface ParticleInvoker {
        void spawn(ServerLevel level,
                   ParticleOptions effect,
                   double x,
                   double y,
                   double z,
                   int count,
                   double offsetX,
                   double offsetY,
                   double offsetZ,
                   double speed) throws Throwable;
    }

    private static volatile ParticleInvoker cachedInvoker;

    private VFXCompat() {
    }

    public static void spawnParticles(ServerLevel level,
                                      ParticleOptions effect,
                                      double x,
                                      double y,
                                      double z,
                                      int count,
                                      double offsetX,
                                      double offsetY,
                                      double offsetZ,
                                      double speed) {
        try {
            getInvoker().spawn(level, effect, x, y, z, count, offsetX, offsetY, offsetZ, speed);
        } catch (Throwable throwable) {
            throw new RuntimeException("Unable to spawn particles via compatibility bridge", throwable);
        }
    }

    private static ParticleInvoker getInvoker() {
        ParticleInvoker invoker = cachedInvoker;
        if (invoker != null) {
            return invoker;
        }
        synchronized (VFXCompat.class) {
            if (cachedInvoker == null) {
                cachedInvoker = resolveInvoker();
            }
            return cachedInvoker;
        }
    }

    private static ParticleInvoker resolveInvoker() {
        List<Method> candidates = new ArrayList<>();
        candidates.addAll(Arrays.asList(ServerLevel.class.getMethods()));
        candidates.addAll(Arrays.asList(ServerLevel.class.getDeclaredMethods()));

        for (Method method : candidates) {
            String name = method.getName();
            if (!"spawnParticles".equals(name) && !"sendParticles".equals(name)) {
                continue;
            }
            ParticleInvoker invoker = tryBuildInvoker(method);
            if (invoker != null) {
                return invoker;
            }
        }
        throw new NoSuchMethodError("No suitable spawnParticles/sendParticles overload found for this environment");
    }

    private static ParticleInvoker tryBuildInvoker(Method method) {
        Class<?>[] types = method.getParameterTypes();
        int particleIndex = findParticleParameter(types);
        if (particleIndex < 0) {
            return null;
        }

        for (int i = 0; i < particleIndex; i++) {
            if (types[i] != boolean.class) {
                return null;
            }
        }

        int intIndex = -1;
        int booleanCount = 0;
        int doubleCount = 0;
        for (int i = particleIndex + 1; i < types.length; i++) {
            Class<?> type = types[i];
            if (type == int.class) {
                if (intIndex >= 0) {
                    return null;
                }
                intIndex = i;
            } else if (type == double.class) {
                doubleCount++;
            } else if (type == boolean.class) {
                booleanCount++;
            } else {
                return null;
            }
        }

        // We expect at least xyz + offsetXYZ + speed (7 doubles) after the particle parameter.
        if (doubleCount < 7 || booleanCount > 2) {
            return null;
        }

        method.setAccessible(true);
        final boolean hasCountParameter = intIndex >= 0;
        return (level, effect, x, y, z, count, offsetX, offsetY, offsetZ, speed) -> {
            Object[] args = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                if (types[i] == boolean.class) {
                    args[i] = Boolean.FALSE;
                }
            }
            args[particleIndex] = effect;

            double[] doubles = {x, y, z, offsetX, offsetY, offsetZ, speed};
            int doubleCursor = 0;
            for (int i = particleIndex + 1; i < types.length; i++) {
                if (types[i] == double.class) {
                    if (doubleCursor >= doubles.length) {
                        return;
                    }
                    args[i] = doubles[doubleCursor++];
                } else if (types[i] == int.class) {
                    args[i] = count;
                }
            }

            if (!hasCountParameter) {
                for (int i = 0; i < Math.max(1, count); i++) {
                    method.invoke(level, args);
                }
            } else {
                method.invoke(level, args);
            }
        };
    }

    private static int findParticleParameter(Class<?>[] types) {
        for (int i = 0; i < types.length; i++) {
            if (ParticleOptions.class.isAssignableFrom(types[i])) {
                return i;
            }
        }
        return -1;
    }
}

