package com.myudog.myulib.api.core.animation;

public enum Easing {
    LINEAR {
        @Override
        public double apply(double progress) {
            return clamp(progress);
        }
    },
    EASE_IN_QUAD {
        @Override
        public double apply(double progress) {
            double t = clamp(progress);
            return t * t;
        }
    },
    EASE_OUT_QUAD {
        @Override
        public double apply(double progress) {
            double t = clamp(progress);
            return t * (2.0 - t);
        }
    },
    EASE_IN_OUT_QUAD {
        @Override
        public double apply(double progress) {
            double t = clamp(progress);
            if (t < 0.5) {
                return 2.0 * t * t;
            }
            return 1.0 - Math.pow(-2.0 * t + 2.0, 2.0) / 2.0;
        }
    },
    EASE_IN_CUBIC {
        @Override
        public double apply(double progress) {
            double t = clamp(progress);
            return t * t * t;
        }
    },
    EASE_OUT_CUBIC {
        @Override
        public double apply(double progress) {
            double t = clamp(progress) - 1.0;
            return t * t * t + 1.0;
        }
    },
    EASE_IN_OUT_CUBIC {
        @Override
        public double apply(double progress) {
            double t = clamp(progress);
            if (t < 0.5) {
                return 4.0 * t * t * t;
            }
            return 1.0 - Math.pow(-2.0 * t + 2.0, 3.0) / 2.0;
        }
    },
    SMOOTH_STEP {
        @Override
        public double apply(double progress) {
            double t = clamp(progress);
            return t * t * (3.0 - 2.0 * t);
        }
    };

    public abstract double apply(double progress);

    public float apply(float progress) {
        return (float) apply((double) progress);
    }

    public static double clamp(double progress) {
        if (progress < 0.0) {
            return 0.0;
        }
        if (progress > 1.0) {
            return 1.0;
        }
        return progress;
    }
}
