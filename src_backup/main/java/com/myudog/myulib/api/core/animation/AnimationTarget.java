package com.myudog.myulib.api.core.animation;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface AnimationTarget<T> {
    void apply(T value);

    static <T> AnimationTarget<T> of(Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        return consumer::accept;
    }
}

