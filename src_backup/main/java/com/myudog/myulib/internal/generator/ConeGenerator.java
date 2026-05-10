package com.myudog.myulib.internal.generator;
import com.myudog.myulib.api.Shapes;
import com.myudog.myulib.api.core.shape.IShape;
import net.minecraft.world.phys.Vec3;import java.util.ArrayList;
import java.util.List;
public final class ConeGenerator implements IShape {
    @Override
    public List<Vec3> getOutlinePoints(Vec3 size, double density) {
        List<Vec3> points = new ArrayList<>();
        double radiusX = size.x;
        double radiusZ = size.z;
        double height = size.y;
        int circleCount = Math.max(8, (int) Math.round(2.0 * Math.PI * Math.max(radiusX, radiusZ) * Math.max(1.0, density)));
        for (int i = 0; i < circleCount; i++) {
            double angle = 2.0 * Math.PI * i / circleCount;
            points.add(new Vec3(radiusX * Math.cos(angle), 0.0, radiusZ * Math.sin(angle)));
        }
        for (int i = 0; i < 8; i++) {
            double angle = 2.0 * Math.PI * i / 8.0;
            Vec3 basePos = new Vec3(radiusX * Math.cos(angle), 0.0, radiusZ * Math.sin(angle));
            Vec3 tipPos = new Vec3(0.0, height, 0.0);
            int lineDensity = Math.max(1, (int) Math.round(height * Math.max(1.0, density)));
            for (int j = 0; j <= lineDensity; j++) {
                double t = j / (double) lineDensity;
                points.add(new Vec3(
                        basePos.x + (tipPos.x - basePos.x) * t,
                        basePos.y + (tipPos.y - basePos.y) * t,
                        basePos.z + (tipPos.z - basePos.z) * t
                ));
            }
        }
        return points;
    }
    @Override
    public List<Vec3> getSolidPoints(Vec3 size, double density) {
        List<Vec3> points = new ArrayList<>();
        double hStep = 1.0 / Math.max(0.1, density);
        for (double h = 0.0; h <= size.y; h += hStep) {
            double ratio = size.y <= 0.0 ? 0.0 : 1.0 - (h / size.y);
            Vec3 currentRadius = new Vec3(size.x * ratio, 0.0, size.z * ratio);
            for (Vec3 point : Shapes.CIRCLE.getSolidPoints(currentRadius, density)) {
                points.add(new Vec3(point.x, h, point.z));
            }
        }
        return points;
    }
}