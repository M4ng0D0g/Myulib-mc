package com.myudog.myulib.client.api.ui.network;

import java.util.concurrent.atomic.AtomicInteger;

public final class ConfigUiClientState {
    private static volatile boolean readonly = true;
    private static volatile String snapshotJson = "{}";
    private static volatile boolean lastApplySuccess = false;
    private static volatile String lastApplyMessage = "";
    private static final AtomicInteger REVISION = new AtomicInteger(0);

    private ConfigUiClientState() {
    }

    public static void updateSnapshot(boolean readonlyValue, String snapshot) {
        readonly = readonlyValue;
        snapshotJson = snapshot == null ? "{}" : snapshot;
        REVISION.incrementAndGet();
    }

    public static void updateApplyResult(boolean success, String message, boolean readonlyValue, String snapshot) {
        lastApplySuccess = success;
        lastApplyMessage = message == null ? "" : message;
        updateSnapshot(readonlyValue, snapshot);
    }

    public static int revision() {
        return REVISION.get();
    }

    public static boolean readonly() {
        return readonly;
    }

    public static String snapshotJson() {
        return snapshotJson;
    }

    public static boolean lastApplySuccess() {
        return lastApplySuccess;
    }

    public static String lastApplyMessage() {
        return lastApplyMessage;
    }
}

