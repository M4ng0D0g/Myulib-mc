package com.myudog.myulib.internal.dynamics;

import com.myudog.myulib.api.core.dynamics.IForceField;
import net.minecraft.world.phys.Vec3;

public final class RadialForceField implements IForceField {
    private final boolean attractive;
    private final double maxRange;

    public RadialForceField(boolean attractive) {
        this(attractive, 10.0);
    }

    public RadialForceField(boolean attractive, double maxRange) {
        this.attractive = attractive;
        this.maxRange = Math.max(0.0001, maxRange);
    }

    @Override
    public Vec3 calculateForce(Vec3 pos, Vec3 center, double strength) {
        Vec3 direction = attractive ? center.subtract(pos) : pos.subtract(center);
        double distanceSq = direction.lengthSqr();
        if (distanceSq < 0.0001 || distanceSq > maxRange * maxRange) {
            return Vec3.ZERO;
        }

        double distance = Math.sqrt(distanceSq);
        double falloff = (maxRange - distance) / maxRange;
        double magnitude = strength * falloff;
        return direction.normalize().scale(magnitude);
    }
}
