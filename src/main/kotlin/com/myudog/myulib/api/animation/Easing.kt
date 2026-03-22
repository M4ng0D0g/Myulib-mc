package com.myudog.myulib.api.animation

import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.PI

enum class Easing {
    LINEAR {
        override fun calculate(t: Double) = t
    },
    QUAD_OUT {
        override fun calculate(t: Double) = 1 - (1 - t) * (1 - t)
    },
    ELASTIC_OUT {
        override fun calculate(t: Double): Double {
            val c4 = (2 * PI) / 3
            return if (t == 0.0) 0.0 else if (t == 1.0) 1.0
            else 2.0.pow(-10 * t) * sin((t * 10 - 0.75) * c4) + 1
        }
    };

    abstract fun calculate(t: Double): Double
}