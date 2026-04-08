package com.myudog.myulib.api.game.bootstrap;

import com.myudog.myulib.api.game.object.GameObjectKind;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.Objects;

public record GameObjectConfig(Identifier id, GameObjectKind kind, Identifier type, String name, boolean required, Map<String, String> properties) {
    public GameObjectConfig {
        Objects.requireNonNull(id, "id");
        kind = kind == null ? GameObjectKind.CUSTOM : kind;
        properties = properties == null ? Map.of() : Map.copyOf(properties);
    }

    public GameObjectConfig(Identifier id) {
        this(id, GameObjectKind.CUSTOM, null, null, true, Map.of());
    }

    public GameObjectConfig(Identifier id, Identifier type, String name, boolean required) {
        this(id, GameObjectKind.CUSTOM, type, name, required, Map.of());
    }

    public GameObjectConfig(Identifier id, GameObjectKind kind, Identifier type, String name, boolean required) {
        this(id, kind, type, name, required, Map.of());
    }
}


