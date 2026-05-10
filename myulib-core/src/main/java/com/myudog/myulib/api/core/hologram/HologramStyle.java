package com.myudog.myulib.api.core.hologram;

/**
 * 封裝全息投影的視覺樣式
 */
public record HologramStyle(
        int color, // ARGB 顏色
        byte flags // 功能旗標
) {
    public static final int FLAG_POINTS  = 1;
    public static final int FLAG_LINES   = 1 << 1;
    public static final int FLAG_FACES   = 1 << 2;
    public static final int FLAG_NAME    = 1 << 3;
    public static final int FLAG_AXES    = 1 << 4;
    public static final int FLAG_CORNERS = 1 << 5;

    // --- 預設樣式 ---

    public static HologramStyle defaults() {
        // 預設：青色 (Cyan) 框線與座標軸
        return new HologramStyle(0xFF00FFFF, (byte)(FLAG_LINES | FLAG_AXES));
    }

    public static HologramStyle full() {
        // 全部開啟
        return new HologramStyle(0x7700FF00, (byte)0xFF);
    }

    public static HologramStyle labelsOnly() {
        // 僅顯示名稱
        return new HologramStyle(0xFFFFFFFF, (byte)FLAG_NAME);
    }

    // --- 流式 API (Fluent API) ---

    public HologramStyle withFeature(HologramFeature feature, boolean enabled) {
        int bit = getBit(feature);
        int newFlags = enabled ? (flags | bit) : (flags & ~bit);
        return new HologramStyle(this.color, (byte) newFlags);
    }

    public boolean isEnabled(HologramFeature feature) {
        return (flags & getBit(feature)) != 0;
    }

    public boolean showPoints() { return isEnabled(HologramFeature.POINTS); }

    public boolean showLines() { return isEnabled(HologramFeature.LINES); }

    public boolean showFaces() { return isEnabled(HologramFeature.FACES); }

    public boolean showName() { return isEnabled(HologramFeature.NAME); }

    public boolean showAxes() { return isEnabled(HologramFeature.AXES); }

    private int getBit(HologramFeature feature) {
        return switch (feature) {
            case POINTS -> FLAG_POINTS;
            case LINES -> FLAG_LINES;
            case FACES -> FLAG_FACES;
            case NAME -> FLAG_NAME;
            case AXES -> FLAG_AXES;
            case CORNERS -> FLAG_CORNERS;
        };
    }
}