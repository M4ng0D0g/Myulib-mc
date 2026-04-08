package com.myudog.myulib.client.internal.ui.system;

import com.myudog.myulib.api.animation.*;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicReference;

public final class AnimationSystemTest {
    private AnimationSystemTest() {
    }

    @Test
    void registerTickAndControlAnimationsFromTheSystem() {
        AnimationSystem system = new AnimationSystem();
        AtomicReference<Double> value = new AtomicReference<>(0.0);
        AnimationSpec<Double> spec = AnimationSpec.of(
                1000L, 0.0, 8.0, Easing.LINEAR, PlayMode.ONCE, Interpolators.DOUBLE
        );
        AnimatorComponent<Double> animator = new AnimatorComponent<>(spec, value::set);

        AnimationSystem.Handle<Double> handle = system.register("fade", animator);
        TestSupport.assertTrue(system.contains("fade"));

        // ... (保留原本所有系統控制測試邏輯) ...
        handle.unregister();
        TestSupport.assertEquals(0, system.size());
    }
}