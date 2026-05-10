package com.myudog.myulib.api;

import com.myudog.myulib.api.core.control.ControlManager;
import com.myudog.myulib.api.core.debug.DebugLogManager;
import com.myudog.myulib.api.core.hologram.network.HologramNetworking;
import com.myudog.myulib.api.core.camera.CameraApi;
import com.myudog.myulib.api.core.timer.TimerManager;
import com.myudog.myulib.api.framework.ui.network.ConfigUiNetworking;

public final class MyulibApi {
    public static void initCore() {
        AccessSystems.init();
        DebugLogManager.INSTANCE.install();

        CameraApi.initServer();
        ControlManager.INSTANCE.install();
        HologramNetworking.registerPayloads();
        ConfigUiNetworking.registerPayloads();
        ConfigUiNetworking.registerServerReceivers();

        TimerManager.INSTANCE.install();
    }

    public static void initFramework() {
        // Framework-level initialization (permission systems, game registry, etc.)
    }
}
