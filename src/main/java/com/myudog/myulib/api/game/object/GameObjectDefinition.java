package com.myudog.myulib.api.game.object;

import net.minecraft.resources.Identifier;

import java.util.Map;

public record GameObjectDefinition(
    Identifier id,
    GameObjectKind kind,
    Identifier type,
    String name,
    boolean required,
    Map<String, String> properties
) {
    public GameObjectDefinition {
        properties = properties == null ? Map.of() : Map.copyOf(properties);
        kind = kind == null ? GameObjectKind.CUSTOM : kind;
    }

    public GameObjectDefinition(Identifier id) {
        this(id, GameObjectKind.CUSTOM, null, null, true, Map.of());
    }

    public GameObjectDefinition(Identifier id, GameObjectKind kind, Identifier type, String name, boolean required) {
        this(id, kind, type, name, required, Map.of());
    }
}



