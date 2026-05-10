package com.myudog.myulib.api.core.animation;

public final class Interpolators {
    public static final ValueInterpolator<Double> DOUBLE = (startValue, endValue, progress) ->
        startValue + (endValue - startValue) * Easing.clamp(progress);

    public static final ValueInterpolator<Float> FLOAT = (startValue, endValue, progress) ->
        (float) (startValue + (endValue - startValue) * Easing.clamp(progress));

    public static final ValueInterpolator<Integer> INTEGER = (startValue, endValue, progress) -> {
        double value = startValue + (endValue - startValue) * Easing.clamp(progress);
        return (int) Math.round(value);
    };

    private Interpolators() {
    }
}

