package com.myudog.myulib.api.game.bootstrap;

import net.minecraft.resources.Identifier;

import java.util.Map;

public record GameBootstrapConfig(Map<Identifier, GameObjectConfig> specialObjects, Map<String, String> metadata) {
    public GameBootstrapConfig {
        specialObjects = specialObjects == null ? Map.of() : Map.copyOf(specialObjects);
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }

    public GameBootstrapConfig() {
        this(Map.of(), Map.of());
    }
}


