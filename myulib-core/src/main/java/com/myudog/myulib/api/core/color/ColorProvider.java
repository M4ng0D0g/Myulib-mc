package com.myudog.myulib.api.core.color;
import org.joml.Vector3f;
import java.awt.Color;
public final class ColorProvider {
    private ColorProvider() {
    }
    public static Vector3f hexToRGB(String hex) {
        Color color = Color.decode(hex.startsWith("#") ? hex : "#" + hex);
        return new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }
    public static Vector3f lerp(Vector3f start, Vector3f end, float fraction) {
        return new Vector3f(
                start.x + (end.x - start.x) * fraction,
                start.y + (end.y - start.y) * fraction,
                start.z + (end.z - start.z) * fraction
        );
    }
    public static Vector3f fromHSL(float h, float s, float l) {
        Color color = Color.getHSBColor(h, s, l);
        return new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }
    public static Vector3f getRainbowColor(int tick, int period) {
        return getRainbowColor(tick, period, 1.0f, 1.0f);
    }
    public static Vector3f getRainbowColor(int tick, int period, float saturation, float brightness) {
        int safePeriod = Math.max(1, period);
        float hue = (tick % safePeriod) / (float) safePeriod;
        Color color = Color.getHSBColor(hue, saturation, brightness);
        return new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }
    public static Vector3f pulsateBrightness(Vector3f baseColor, int tick, int period, float minLight) {
        int safePeriod = Math.max(1, period);
        double progress = (tick % safePeriod) / (double) safePeriod;
        float multiplier = minLight + (1.0f - minLight) * (float) (Math.sin(progress * 2.0 * Math.PI) * 0.5 + 0.5);
        return new Vector3f(baseColor.x * multiplier, baseColor.y * multiplier, baseColor.z * multiplier);
    }
}