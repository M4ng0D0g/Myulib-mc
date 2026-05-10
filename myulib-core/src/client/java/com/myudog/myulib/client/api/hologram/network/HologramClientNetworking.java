package com.myudog.myulib.client.api.hologram.network;

import com.myudog.myulib.api.core.hologram.network.HologramNetworking;
import com.myudog.myulib.client.api.hologram.HologramClientManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class HologramClientNetworking {
    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(HologramNetworking.HologramPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                HologramClientManager.INSTANCE.update(payload.entries());
            });
        });
    }
}
