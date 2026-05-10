package com.myudog.myulib.api.core.events;

import com.myudog.myulib.api.core.event.IEvent;
import net.minecraft.server.MinecraftServer;

public class ServerTickEvent implements IEvent {
    private final MinecraftServer server;

    public ServerTickEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}
