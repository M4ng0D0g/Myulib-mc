package com.myudog.myulib.api.shape;
import net.minecraft.world.phys.Vec3;

import java.util.List;
public interface IShape {
    List<Vec3> getOutlinePoints(Vec3 size, double density);
    List<Vec3> getSolidPoints(Vec3 size, double density);
}
