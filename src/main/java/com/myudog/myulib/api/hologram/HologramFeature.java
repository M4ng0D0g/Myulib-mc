package com.myudog.myulib.api.hologram;

import java.util.Locale;

public enum HologramFeature {
    POINTS,
    LINES,     // 完整框線
    FACES,     // 半透明面
    NAME,      // 標籤名稱
    AXES,      // 座標軸 (X, Y, Z)
    CORNERS;   // 結構方塊風格的 L 型角落

    public String token() {
        return name().toLowerCase(Locale.ROOT).replace('_', '-');
    }

    public String id() {
        return token();
    }

    public static HologramFeature parse(String raw) {
        String token = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT).replace('_', '-');
        for (HologramFeature feature : values()) {
            if (feature.token().equals(token)) {
                return feature;
            }
        }
        throw new IllegalArgumentException("Unknown hologram feature: " + raw);
    }
}