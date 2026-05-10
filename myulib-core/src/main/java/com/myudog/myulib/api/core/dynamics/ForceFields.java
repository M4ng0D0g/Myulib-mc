package com.myudog.myulib.api.core.dynamics;

import com.myudog.myulib.internal.dynamics.RadialForceField;
import com.myudog.myulib.internal.dynamics.VortexForceField;
import net.minecraft.world.phys.Vec3;
public final class ForceFields {
    public static final IForceField ATTRACTION = new RadialForceField(true);
    public static final IForceField REPULSION = new RadialForceField(false);
    public static final IForceField VORTEX = new VortexForceField();

    private ForceFields() {
    }

    public static IForceField attraction(double range) {
        return new RadialForceField(true, range);
    }

    public static IForceField repulsion(double range) {
        return new RadialForceField(false, range);
    }

    public static IForceField vortex() {
        return new VortexForceField();
    }

    public static IForceField vortex(Vec3 axis) {
        return new VortexForceField(axis);
    }
}
