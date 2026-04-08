package com.myudog.myulib.api.animation;

import org.junit.jupiter.api.Test;

final class EasingTest {

    @Test
    void linearClampsOutOfRangeProgress() {
        TestSupport.assertEquals(0.0, Easing.LINEAR.apply(-0.5), 1.0e-9);
        TestSupport.assertEquals(1.0, Easing.LINEAR.apply(1.5), 1.0e-9);
    }

    @Test
    void commonCurvesMatchExpectedMidpointValues() {
        TestSupport.assertEquals(0.25, Easing.EASE_IN_QUAD.apply(0.5), 1.0e-9);
        // ... (保留原本所有測試邏輯) ...
        TestSupport.assertEquals(0.5, Easing.SMOOTH_STEP.apply(0.5), 1.0e-9);
    }
}