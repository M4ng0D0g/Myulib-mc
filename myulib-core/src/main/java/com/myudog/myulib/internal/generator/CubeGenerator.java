package com.myudog.myulib.internal.generator;
import com.myudog.myulib.api.core.shape.IShape;
import net.minecraft.world.phys.Vec3;import java.util.ArrayList;
import java.util.List;
public final class CubeGenerator implements IShape {
    @Override
    public List<Vec3> getOutlinePoints(Vec3 size, double density) {
        List<Vec3> points = new ArrayList<>();
        Vec3 half = size.scale(0.5);
        double hx = half.x;
        double hy = half.y;
        double hz = half.z;
        Vec3[] corners = new Vec3[]{
                new Vec3(-hx, -hy, -hz), new Vec3(hx, -hy, -hz), new Vec3(hx, -hy, hz), new Vec3(-hx, -hy, hz),
                new Vec3(-hx, hy, -hz), new Vec3(hx, hy, -hz), new Vec3(hx, hy, hz), new Vec3(-hx, hy, hz)
        };
        for (int i = 0; i < 4; i++) {
            drawLine(points, corners[i], corners[(i + 1) % 4], density);
            drawLine(points, corners[i + 4], corners[(i + 1) % 4 + 4], density);
            drawLine(points, corners[i], corners[i + 4], density);
        }
        return points;
    }
    @Override
    public List<Vec3> getSolidPoints(Vec3 size, double density) {
        List<Vec3> points = new ArrayList<>();
        double step = 1.0 / Math.max(0.1, density);
        for (double x = -size.x / 2.0; x <= size.x / 2.0; x += step) {
            for (double y = -size.y / 2.0; y <= size.y / 2.0; y += step) {
                for (double z = -size.z / 2.0; z <= size.z / 2.0; z += step) {
                    points.add(new Vec3(x, y, z));
                }
            }
        }
        return points;
    }
    private static void drawLine(List<Vec3> points, Vec3 start, Vec3 end, double density) {
        double dist = start.distanceTo(end);
        int count = Math.max(1, (int) Math.round(dist * Math.max(1.0, density)));
        for (int i = 0; i <= count; i++) {
            double t = i / (double) count;
            points.add(new Vec3(
                    start.x + (end.x - start.x) * t,
                    start.y + (end.y - start.y) * t,
                    start.z + (end.z - start.z) * t
            ));
        }
    }
}