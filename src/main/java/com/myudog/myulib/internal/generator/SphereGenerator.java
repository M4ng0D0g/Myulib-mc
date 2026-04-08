package com.myudog.myulib.internal.generator;
import com.myudog.myulib.api.shape.IShape;
import net.minecraft.world.phys.Vec3;import java.util.ArrayList;
import java.util.List;
public final class SphereGenerator implements IShape {
    @Override
    public List<Vec3> getOutlinePoints(Vec3 size, double density) {
        List<Vec3> points = new ArrayList<>();
        double radius = size.x;
        int count = Math.max(12, (int) Math.round(4.0 * Math.PI * radius * radius * Math.max(1.0, density)));
        for (int i = 0; i < count; i++) {
            double phi = Math.acos(1.0 - 2.0 * (i + 0.5) / count);
            double theta = Math.PI * (1.0 + Math.sqrt(5.0)) * (i + 0.5);
            points.add(new Vec3(
                    radius * Math.sin(phi) * Math.cos(theta),
                    radius * Math.cos(phi),
                    radius * Math.sin(phi) * Math.sin(theta)
            ));
        }
        return points;
    }
    @Override
    public List<Vec3> getSolidPoints(Vec3 size, double density) {
        List<Vec3> points = new ArrayList<>();
        double r = 0.1;
        double step = 1.0 / Math.max(0.1, density * 5.0);
        while (r <= size.x) {
            points.addAll(getOutlinePoints(new Vec3(r, r, r), density));
            r += step;
        }
        return points;
    }
}