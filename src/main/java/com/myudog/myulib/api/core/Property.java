package com.myudog.myulib.api.core;

import java.util.function.Function;

public record Property<T>(
        String name,
        Class<T> type,
        Function<String, T> parser
) {
    public T parse(String input) {
        return parser.apply(input);
    }
}