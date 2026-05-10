package com.myudog.myulib.api.core.dynamics;
import net.minecraft.world.phys.Vec3;public interface IForceField {;
    Vec3 calculateForce(Vec3 pos, Vec3 center, double strength);
}