package com.myudog.myulib.internal.dynamics;

import com.myudog.myulib.api.core.dynamics.IForceField;
import net.minecraft.world.phys.Vec3;

public final class VortexForceField implements IForceField {
    private final Vec3 axis;

    public VortexForceField() {
        this(new Vec3(0.0, 1.0, 0.0));
    }

    public VortexForceField(Vec3 axis) {
        Vec3 normalized = axis == null ? new Vec3(0.0, 1.0, 0.0) : axis;
        double lengthSq = normalized.lengthSqr();
        this.axis = lengthSq < 0.0001 ? new Vec3(0.0, 1.0, 0.0) : normalized.normalize();
    }

    @Override
    public Vec3 calculateForce(Vec3 pos, Vec3 center, double strength) {
        Vec3 relativePos = pos.subtract(center);
        if (relativePos.lengthSqr() < 0.0001) {
            return Vec3.ZERO;
        }

        Vec3 tangent = relativePos.cross(axis);
        if (tangent.lengthSqr() < 0.0001) {
            return Vec3.ZERO;
        }

        return tangent.normalize().scale(strength);
    }
}
