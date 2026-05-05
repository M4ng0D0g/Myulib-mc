package com.myudog.myulib.api.core;

public interface ReversibleAction {

    void execute() throws Exception;
    void rollback();

    static ReversibleAction of(ThrowingRunnable execute, Runnable rollback) {
        return new ReversibleAction() {
            @Override
            public void execute() throws Exception {
                execute.run();
            }
            @Override
            public void rollback() {
                rollback.run();
            }
        };
    }
}
