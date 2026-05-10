package com.myudog.myulib.api.core.debug;

import java.util.Locale;

public enum DebugFeature {
    PERMISSION,
    FIELD,
    ROLEGROUP,
    TEAM,
    GAME,
    TIMER,
    CONTROL,
    CAMERA,
    COMMAND;

    public static DebugFeature parse(String raw) {
        return DebugFeature.valueOf(raw.toUpperCase().replace('-', '_'));
    }

    public String token() {
        return name().toLowerCase(Locale.ROOT).replace('_', '-');
    }

    public String id() {
        return token();
    }
}

