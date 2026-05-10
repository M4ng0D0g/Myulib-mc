package com.myudog.myulib.api.core.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class NbtIoHelper {
    private NbtIoHelper() {
    }

    public static CompoundTag readRoot(Path path) throws Exception {
        for (String methodName : List.of("readCompressed", "read")) {
            for (Method method : NbtIo.class.getMethods()) {
                if (!methodName.equals(method.getName()) || !Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                try {
                    Class<?>[] parameters = method.getParameterTypes();
                    if (parameters.length == 1 && Path.class.isAssignableFrom(parameters[0])) {
                        Object value = method.invoke(null, path);
                        if (value instanceof CompoundTag tag) {
                            return tag;
                        }
                    }
                    if (parameters.length == 2 && Path.class.isAssignableFrom(parameters[0])) {
                        Object helper = createHelperArgument(parameters[1]);
                        Object value = method.invoke(null, path, helper);
                        if (value instanceof CompoundTag tag) {
                            return tag;
                        }
                    }
                    if (parameters.length == 1 && InputStream.class.isAssignableFrom(parameters[0])) {
                        try (InputStream inputStream = Files.newInputStream(path)) {
                            Object value = method.invoke(null, inputStream);
                            if (value instanceof CompoundTag tag) {
                                return tag;
                            }
                        }
                    }
                    if (parameters.length == 2 && InputStream.class.isAssignableFrom(parameters[0])) {
                        Object helper = createHelperArgument(parameters[1]);
                        try (InputStream inputStream = Files.newInputStream(path)) {
                            Object value = method.invoke(null, inputStream, helper);
                            if (value instanceof CompoundTag tag) {
                                return tag;
                            }
                        }
                    }
                } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
                    // Try the next overload when mappings/runtime signatures differ.
                }
            }
        }
        throw new NoSuchMethodException("No suitable NbtIo read method found");
    }

    public static void writeRoot(Path path, CompoundTag root) throws Exception {
        for (String methodName : List.of("writeCompressed", "write")) {
            for (Method method : NbtIo.class.getMethods()) {
                if (!methodName.equals(method.getName()) || !Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                try {
                    Class<?>[] parameters = method.getParameterTypes();
                    if (parameters.length == 2
                            && CompoundTag.class.isAssignableFrom(parameters[0])
                            && Path.class.isAssignableFrom(parameters[1])) {
                        method.invoke(null, root, path);
                        return;
                    }
                    if (parameters.length == 2
                            && CompoundTag.class.isAssignableFrom(parameters[0])
                            && OutputStream.class.isAssignableFrom(parameters[1])) {
                        try (OutputStream outputStream = Files.newOutputStream(path)) {
                            method.invoke(null, root, outputStream);
                            return;
                        }
                    }
                    if (parameters.length == 3
                            && CompoundTag.class.isAssignableFrom(parameters[0])
                            && OutputStream.class.isAssignableFrom(parameters[1])) {
                        Object helper = createHelperArgument(parameters[2]);
                        try (OutputStream outputStream = Files.newOutputStream(path)) {
                            method.invoke(null, root, outputStream, helper);
                            return;
                        }
                    }
                } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
                    // Try the next overload when mappings/runtime signatures differ.
                }
            }
        }
        throw new NoSuchMethodException("No suitable NbtIo write method found");
    }

    public static Path resolveRootPath(MinecraftServer server) {
        if (server == null) {
            return Paths.get(".");
        }
        for (String methodName : List.of("getSavePath", "getRunDirectory", "getServerRunDirectory", "getServerDirectory")) {
            try {
                for (Method method : server.getClass().getMethods()) {
                    if (!method.getName().equals(methodName)) {
                        continue;
                    }
                    if (method.getParameterCount() == 0 && Path.class.isAssignableFrom(method.getReturnType())) {
                        return (Path) method.invoke(server);
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return Paths.get(".");
    }

    public static List<String> keysOf(CompoundTag compound) {
        for (String methodName : List.of("getKeys", "getAllKeys", "keySet")) {
            try {
                Method method = compound.getClass().getMethod(methodName);
                Object value = method.invoke(compound);
                if (value instanceof Iterable<?> iterable) {
                    List<String> keys = new ArrayList<>();
                    for (Object entry : iterable) {
                        keys.add(String.valueOf(entry));
                    }
                    return keys;
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return List.of();
    }

    private static Object createHelperArgument(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return Boolean.FALSE;
            }
            if (type == int.class) {
                return 0;
            }
            if (type == long.class) {
                return 0L;
            }
            if (type == float.class) {
                return 0.0f;
            }
            if (type == double.class) {
                return 0.0d;
            }
            if (type == byte.class) {
                return (byte) 0;
            }
            if (type == short.class) {
                return (short) 0;
            }
            if (type == char.class) {
                return (char) 0;
            }
            return null;
        }

        for (String fieldName : List.of("INSTANCE", "UNLIMITED", "UNLIMITED_HEAP")) {
            try {
                var field = type.getField(fieldName);
                if (Modifier.isStatic(field.getModifiers()) && type.isAssignableFrom(field.getType())) {
                    return field.get(null);
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }

        try {
            for (Method method : type.getMethods()) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                if (method.getParameterCount() == 0 && type.isAssignableFrom(method.getReturnType())) {
                    return method.invoke(null);
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }

        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}

