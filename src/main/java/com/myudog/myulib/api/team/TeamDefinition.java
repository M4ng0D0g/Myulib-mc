package com.myudog.myulib.api.team;

import java.util.Map;
import java.util.Objects;

public record TeamDefinition(String id, String displayName, String color, Map<String, String> metadata) {
    public TeamDefinition {
        id = Objects.requireNonNull(id, "id");
        displayName = displayName == null ? id : displayName;
        color = color == null ? "default" : color;
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}

