package com.myudog.myulib.client.api.framework.ui.network;

import com.myudog.myulib.api.framework.ui.network.ConfigUiNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class ConfigUiClientNetworking {
    private static boolean installed;

    private ConfigUiClientNetworking() {
    }

    public static void install() {
        if (installed) {
            return;
        }
        installed = true;

        ClientPlayNetworking.registerGlobalReceiver(ConfigUiNetworking.ConfigSnapshotPayload.TYPE,
                (payload, context) -> context.client().execute(() ->
                        ConfigUiClientState.updateSnapshot(payload.readonly(), payload.snapshotJson())));

        ClientPlayNetworking.registerGlobalReceiver(ConfigUiNetworking.ConfigApplyResultPayload.TYPE,
                (payload, context) -> context.client().execute(() ->
                        ConfigUiClientState.updateApplyResult(payload.success(), payload.message(), payload.readonly(), payload.snapshotJson())));
    }
}

