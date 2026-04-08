package com.myudog.myulib.internal.generator;
import com.myudog.myulib.api.shape.IShape;
import net.minecraft.world.phys.Vec3;import java.util.ArrayList;
import java.util.List;
public final class CircleGenerator implements IShape {
    @Override
    public List<Vec3> getOutlinePoints(Vec3 size, double density) {
        double radius = size.x;
        int count = Math.max(3, (int) Math.round(density));
        List<Vec3> points = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            double angle = 2.0 * Math.PI * i / count;
            points.add(new Vec3(radius * Math.cos(angle), 0.0, radius * Math.sin(angle)));
        }
        return points;
    }
    @Override
    public List<Vec3> getSolidPoints(Vec3 size, double density) {
        double radius = size.x;
        int rings = Math.max(1, (int) Math.round(density / 2.0));
        int segments = Math.max(6, (int) Math.round(density));
        List<Vec3> points = new ArrayList<>();
        for (int r = 0; r <= rings; r++) {
            double rr = radius * (r / (double) rings);
            for (int i = 0; i < segments; i++) {
                double angle = 2.0 * Math.PI * i / segments;
                points.add(new Vec3(rr * Math.cos(angle), 0.0, rr * Math.sin(angle)));
            }
        }
        return points;
    }
}