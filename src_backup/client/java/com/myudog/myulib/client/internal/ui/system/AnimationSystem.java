package com.myudog.myulib.client.internal.ui.system;

import com.myudog.myulib.api.core.animation.AnimatorComponent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class AnimationSystem {
    private final Map<String, AnimatorComponent<?>> animators = new LinkedHashMap<>();
    private long totalTickedMillis;

    public long totalTickedMillis() {
        return totalTickedMillis;
    }

    public int size() {
        return animators.size();
    }

    public boolean contains(String id) {
        return animators.containsKey(normalize(id));
    }

    public <T> Handle<T> register(String id, AnimatorComponent<T> animator) {
        String key = normalize(id);
        Objects.requireNonNull(animator, "animator");
        if (animators.containsKey(key)) {
            throw new IllegalArgumentException("Animation already registered: " + key);
        }
        animators.put(key, animator);
        return new Handle<>(this, key);
    }

    public <T> AnimatorComponent<T> animator(String id) {
        return castAnimator(requireAnimator(id));
    }

    public void unregister(String id) {
        animators.remove(normalize(id));
    }

    public void clear() {
        animators.clear();
        totalTickedMillis = 0L;
    }

    public boolean play(String id) {
        AnimatorComponent<?> animator = animators.get(normalize(id));
        if (animator == null) {
            return false;
        }
        animator.play();
        return true;
    }

    public boolean pause(String id) {
        AnimatorComponent<?> animator = animators.get(normalize(id));
        if (animator == null) {
            return false;
        }
        animator.pause();
        return true;
    }

    public boolean resume(String id) {
        AnimatorComponent<?> animator = animators.get(normalize(id));
        if (animator == null) {
            return false;
        }
        animator.resume();
        return true;
    }

    public boolean stop(String id) {
        AnimatorComponent<?> animator = animators.get(normalize(id));
        if (animator == null) {
            return false;
        }
        animator.stop();
        return true;
    }

    public void tick(long deltaMillis) {
        if (deltaMillis < 0L) {
            throw new IllegalArgumentException("deltaMillis must be >= 0");
        }
        totalTickedMillis += deltaMillis;
        for (AnimatorComponent<?> animator : animators.values()) {
            animator.tick(deltaMillis);
        }
    }

    private AnimatorComponent<?> requireAnimator(String id) {
        String key = normalize(id);
        AnimatorComponent<?> animator = animators.get(key);
        if (animator == null) {
            throw new IllegalArgumentException("Unknown animation: " + key);
        }
        return animator;
    }

    private static String normalize(String id) {
        return Objects.requireNonNull(id, "id");
    }

    @SuppressWarnings("unchecked")
    private static <T> AnimatorComponent<T> castAnimator(AnimatorComponent<?> animator) {
        return (AnimatorComponent<T>) animator;
    }

    public static final class Handle<T> {
        private final AnimationSystem system;
        private final String id;

        private Handle(AnimationSystem system, String id) {
            this.system = system;
            this.id = id;
        }

        public String id() {
            return id;
        }

        public AnimatorComponent<T> animator() {
            return system.animator(id);
        }

        public void play() {
            system.play(id);
        }

        public void pause() {
            system.pause(id);
        }

        public void resume() {
            system.resume(id);
        }

        public void stop() {
            system.stop(id);
        }

        public void unregister() {
            system.unregister(id);
        }
    }
}

