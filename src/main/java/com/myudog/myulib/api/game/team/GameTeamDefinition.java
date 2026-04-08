package com.myudog.myulib.api.game.team;

import net.minecraft.resources.Identifier;

import java.util.Map;

public record GameTeamDefinition(
    Identifier id,
    String displayName,
    GameTeamColor color,
    boolean friendlyFire,
    boolean seeFriendlyInvisibles,
    Map<String, String> properties
) {
    public GameTeamDefinition {
        color = color == null ? GameTeamColor.DEFAULT : color;
        properties = properties == null ? Map.of() : Map.copyOf(properties);
    }

    public GameTeamDefinition(Identifier id, String displayName) {
        this(id, displayName, GameTeamColor.DEFAULT, true, true, Map.of());
    }
}



