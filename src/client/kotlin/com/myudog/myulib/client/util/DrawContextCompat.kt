package com.myudog.myulib.client.util

import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import java.lang.reflect.Method

/** Compatibility helpers for DrawContext API differences across mappings/versions. */
object DrawContextCompat {
    private val drawItemInGuiMethods: List<Method> by lazy {
        DrawContext::class.java.declaredMethods.filter { it.name.startsWith("drawItemInGui") || it.name == "drawItemInGuiWithOverrides" }
    }

    /** Try to invoke an available drawItemInGui... method; fallback to drawItem() if unavailable. */
    fun drawItemInGuiWithOverridesCompat(ctx: DrawContext, textRenderer: Any, stack: ItemStack, x: Int, y: Int) {
        // Try reflection for existing methods
        for (m in drawItemInGuiMethods) {
            try {
                m.isAccessible = true
                // Attempt to invoke; common signatures include (TextRenderer, ItemStack, int, int)
                m.invoke(ctx, textRenderer, stack, x, y)
                return
            } catch (_: Throwable) {
                // continue
            }
        }

        // Fallback: call drawItem (basic rendering without overlays)
        try {
            ctx.drawItem(stack, x, y)
        } catch (_: Throwable) {
            // give up silently — best-effort compatibility
        }
    }
}

// Extension for convenience
fun DrawContext.drawItemInGuiWithOverridesCompat(textRenderer: Any, stack: ItemStack, x: Int, y: Int) {
    DrawContextCompat.drawItemInGuiWithOverridesCompat(this, textRenderer, stack, x, y)
}


