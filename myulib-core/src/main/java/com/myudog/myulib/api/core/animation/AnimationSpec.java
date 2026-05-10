package com.myudog.myulib.api.core.animation;

import java.util.Objects;

public final class AnimationSpec<T> {
    private final long durationMillis;
    private final T startValue;
    private final T endValue;
    private final Easing easing;
    private final PlayMode playMode;
    private final ValueInterpolator<T> interpolator;
    private final String id;

    private AnimationSpec(Builder<T> builder) {
        this.durationMillis = builder.durationMillis;
        this.startValue = builder.startValue;
        this.endValue = builder.endValue;
        this.easing = builder.easing;
        this.playMode = builder.playMode;
        this.interpolator = builder.interpolator;
        this.id = builder.id;
    }

    public static <T> Builder<T> builder(ValueInterpolator<T> interpolator) {
        return new Builder<>(interpolator);
    }

    public static <T> AnimationSpec<T> of(long durationMillis,
                                          T startValue,
                                          T endValue,
                                          Easing easing,
                                          PlayMode playMode,
                                          ValueInterpolator<T> interpolator) {
        return builder(interpolator)
            .durationMillis(durationMillis)
            .startValue(startValue)
            .endValue(endValue)
            .easing(easing)
            .playMode(playMode)
            .build();
    }

    public long durationMillis() {
        return durationMillis;
    }

    public T startValue() {
        return startValue;
    }

    public T endValue() {
        return endValue;
    }

    public Easing easing() {
        return easing;
    }

    public PlayMode playMode() {
        return playMode;
    }

    public ValueInterpolator<T> interpolator() {
        return interpolator;
    }

    public String id() {
        return id;
    }

    public T valueAt(double progress) {
        double easedProgress = easing.apply(progress);
        return interpolator.interpolate(startValue, endValue, easedProgress);
    }

    public T valueAtElapsed(long elapsedMillis) {
        if (durationMillis <= 0L) {
            return endValue;
        }
        return valueAt((double) elapsedMillis / (double) durationMillis);
    }

    public static final class Builder<T> {
        private final ValueInterpolator<T> interpolator;
        private long durationMillis = 250L;
        private T startValue;
        private T endValue;
        private Easing easing = Easing.LINEAR;
        private PlayMode playMode = PlayMode.ONCE;
        private String id = "animation";

        private Builder(ValueInterpolator<T> interpolator) {
            this.interpolator = Objects.requireNonNull(interpolator, "interpolator");
        }

        public Builder<T> durationMillis(long durationMillis) {
            if (durationMillis < 0L) {
                throw new IllegalArgumentException("durationMillis must be >= 0");
            }
            this.durationMillis = durationMillis;
            return this;
        }

        public Builder<T> startValue(T startValue) {
            this.startValue = Objects.requireNonNull(startValue, "startValue");
            return this;
        }

        public Builder<T> endValue(T endValue) {
            this.endValue = Objects.requireNonNull(endValue, "endValue");
            return this;
        }

        public Builder<T> easing(Easing easing) {
            this.easing = Objects.requireNonNull(easing, "easing");
            return this;
        }

        public Builder<T> playMode(PlayMode playMode) {
            this.playMode = Objects.requireNonNull(playMode, "playMode");
            return this;
        }

        public Builder<T> id(String id) {
            this.id = Objects.requireNonNull(id, "id");
            return this;
        }

        public AnimationSpec<T> build() {
            if (startValue == null) {
                throw new IllegalStateException("startValue must be set");
            }
            if (endValue == null) {
                throw new IllegalStateException("endValue must be set");
            }
            return new AnimationSpec<>(this);
        }
    }
}

