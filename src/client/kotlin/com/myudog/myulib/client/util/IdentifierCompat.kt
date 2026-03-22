package com.myudog.myulib.client.util

import net.minecraft.util.Identifier

object IdentifierCompat {
    /** Create an Identifier from namespace and path in a manner tolerant to mapping differences. */
    fun of(namespace: String, path: String): Identifier {
        val combined = "$namespace:$path"
        // Use reflection only to avoid compile-time constructor signature assumptions.
        try {
            val ctor = Identifier::class.java.getDeclaredConstructor(String::class.java)
            ctor.isAccessible = true
            return ctor.newInstance(combined) as Identifier
        } catch (e: Exception) {
            try {
                val ctor2 = Identifier::class.java.getDeclaredConstructor(String::class.java, String::class.java)
                ctor2.isAccessible = true
                return ctor2.newInstance(namespace, path) as Identifier
            } catch (ex: Exception) {
                throw RuntimeException("Unable to construct Identifier for $namespace:$path", ex)
            }
        }
    }
}


