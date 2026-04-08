package com.myudog.myulib.api.animation;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

final class AnimatorComponentTest {

    @Test
    void playPauseResumeAndStopDriveTargetValues() {
        AtomicReference<Double> value = new AtomicReference<>(-1.0);
        AnimationSpec<Double> spec = AnimationSpec.of(
                1000L, 0.0, 10.0, Easing.LINEAR, PlayMode.ONCE, Interpolators.DOUBLE
        );
        AnimatorComponent<Double> animator = new AnimatorComponent<>(spec, value::set);

        TestSupport.assertEquals(PlaybackState.IDLE, animator.state());
        animator.play();
        TestSupport.assertEquals(PlaybackState.PLAYING, animator.state());
        TestSupport.assertEquals(0.0, value.get(), 0.0001);

        animator.tick(250L);
        TestSupport.assertEquals(2.5, value.get(), 0.0001);

        animator.pause();
        TestSupport.assertEquals(PlaybackState.PAUSED, animator.state());
        animator.tick(250L);
        TestSupport.assertEquals(2.5, value.get(), 0.0001);

        animator.resume();
        TestSupport.assertEquals(PlaybackState.PLAYING, animator.state());
        animator.tick(250L);
        TestSupport.assertEquals(5.0, value.get(), 0.0001);

        animator.tick(500L);
        TestSupport.assertEquals(10.0, value.get(), 0.0001);
        TestSupport.assertEquals(PlaybackState.FINISHED, animator.state());

        animator.stop();
        TestSupport.assertEquals(PlaybackState.STOPPED, animator.state());
        TestSupport.assertEquals(0L, animator.elapsedMillis());
        TestSupport.assertEquals(0.0, value.get(), 0.0001);
    }

    @Test
    void pingPongModeAlternatesDirectionAcrossCycles() {
        AtomicReference<Double> value = new AtomicReference<>(0.0);
        AnimationSpec<Double> spec = AnimationSpec.of(
                1000L, 0.0, 10.0, Easing.LINEAR, PlayMode.PING_PONG, Interpolators.DOUBLE
        );
        AnimatorComponent<Double> animator = new AnimatorComponent<>(spec, value::set);

        animator.play();
        animator.tick(250L);
        TestSupport.assertEquals(0L, animator.completedCycles());
        TestSupport.assertTrue(animator.forward());
        TestSupport.assertEquals(2.5, value.get(), 0.0001);

        animator.tick(1000L);
        TestSupport.assertEquals(1L, animator.completedCycles());
        TestSupport.assertFalse(animator.forward());
        TestSupport.assertEquals(7.5, value.get(), 0.0001);
    }
}