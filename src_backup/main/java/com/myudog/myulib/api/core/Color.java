package com.myudog.myulib.api.core;

/**
 * Shared color contract for systems that need named or RGB color values.
 */
public interface Color {
    int rgb();

    default float red() {
        return ((rgb() >> 16) & 0xFF) / 255.0f;
    }

    default float green() {
        return ((rgb() >> 8) & 0xFF) / 255.0f;
    }

    default float blue() {
        return (rgb() & 0xFF) / 255.0f;
    }
}

