package com.myudog.myulib.api.control;

import java.util.Locale;

public enum ControlType {
    MOVE,
    SPRINT,
    SNEAK,
    CRAWL,
    ROTATE,
    JUMP;

    public String token() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String id() {
        return token();
    }

    public static ControlType parse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Control type cannot be blank");
        }

        String normalized = raw.trim()
                .toUpperCase(Locale.ROOT)
                .replace('-', '_');

        if (normalized.startsWith("PLAYER_")) {
            normalized = normalized.substring("PLAYER_".length());
        }

        // Backward-compatible aliases from previous naming variants.
        if ("MOVEMENT".equals(normalized)) {
            normalized = "MOVE";
        } else if ("LOOK".equals(normalized) || "ROTATION".equals(normalized)) {
            normalized = "ROTATE";
        } else if ("SPRINTING".equals(normalized)) {
            normalized = "SPRINT";
        } else if ("SNEAKING".equals(normalized)) {
            normalized = "SNEAK";
        }

        return ControlType.valueOf(normalized);
    }
}

