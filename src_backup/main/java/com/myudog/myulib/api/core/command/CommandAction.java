package com.myudog.myulib.api.core.command;

@FunctionalInterface
public interface CommandAction<C, R> {
    R execute(C context);
}

