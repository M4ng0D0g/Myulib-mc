package com.myudog.myulib.client.api.ui

import com.myudog.myulib.client.util.IdentifierCompat
import net.minecraft.util.Identifier

/** Minimal UI constants to satisfy compilation while real assets are provided later. */
object UiConstants {
    // A safe placeholder identifier for missing icons/textures used during compilation/runtime fallback.
    // Use the single-argument constructor ("namespace:path") to avoid referencing the private two-arg ctor.
    val ICON_PLACEHOLDER: Identifier = IdentifierCompat.of("minecraft", "missing_texture")
}


