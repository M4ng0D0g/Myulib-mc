package com.myudog.myulib.api.core.animation;

@FunctionalInterface
public interface ValueInterpolator<T> {
    T interpolate(T startValue, T endValue, double progress);
}

