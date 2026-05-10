package com.myudog.myulib.api.core;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Exception;
}
