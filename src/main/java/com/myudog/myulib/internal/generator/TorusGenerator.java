package com.myudog.myulib.internal.generator;
import com.myudog.myulib.api.shape.IShape;
import net.minecraft.world.phys.Vec3;import java.util.ArrayList;
import java.util.List;
public final class TorusGenerator implements IShape {
    @Override
    public List<Vec3> getOutlinePoints(Vec3 size, double density) {
        List<Vec3> points = new ArrayList<>();
        double majorR = size.x;
        double minorR = size.y;
        int majorCount = Math.max(12, (int) Math.round(2.0 * Math.PI * majorR * Math.max(1.0, density)));
        int minorCount = Math.max(8, (int) Math.round(2.0 * Math.PI * minorR * Math.max(1.0, density)));
        for (int i = 0; i < majorCount; i++) {
            double theta = 2.0 * Math.PI * i / majorCount;
            for (int j = 0; j < minorCount; j++) {
                double phi = 2.0 * Math.PI * j / minorCount;
                double x = (majorR + minorR * Math.cos(phi)) * Math.cos(theta);
                double y = minorR * Math.sin(phi);
                double z = (majorR + minorR * Math.cos(phi)) * Math.sin(theta);
                points.add(new Vec3(x, y, z));
            }
        }
        return points;
    }
    @Override
    public List<Vec3> getSolidPoints(Vec3 size, double density) {
        List<Vec3> points = new ArrayList<>();
        double r = 0.1;
        double rStep = 1.0 / Math.max(0.1, density);
        while (r <= size.y) {
            points.addAll(getOutlinePoints(new Vec3(size.x, r, 0.0), density));
            r += rStep;
        }
        return points;
    }
}