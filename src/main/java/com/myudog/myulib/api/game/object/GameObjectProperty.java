package com.myudog.myulib.api.game.object;

import java.util.function.Function;

public record GameObjectProperty<T>(
        String name,
        Class<T> type,
        Function<String, T> parser
) {
    public T parse(String input) {
        return parser.apply(input);
    }
}