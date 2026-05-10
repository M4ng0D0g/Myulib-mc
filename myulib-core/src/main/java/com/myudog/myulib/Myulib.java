package com.myudog.myulib;

/**
 * Backward-compat alias for MyulibCore.
 * Used by internal classes that reference Myulib.MOD_ID or Myulib.id().
 */
public final class Myulib {
    public static final String MOD_ID = MyulibCore.MOD_ID;

    public static net.minecraft.resources.Identifier id(String path) {
        return MyulibCore.id(path);
    }

    private Myulib() {}
}
