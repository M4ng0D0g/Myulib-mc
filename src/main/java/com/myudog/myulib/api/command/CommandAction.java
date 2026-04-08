package com.myudog.myulib.api.command;

@FunctionalInterface
public interface CommandAction<C, R> {
    R execute(C context);
}

