package com.myudog.myulib.api.core.floating;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public interface IFloatingObject {
	void spawn(Vec3 pos);

	void remove();

	void moveTo(Vec3 pos, int interpolationDuration);

	void setScale(Vector3f scale, int interpolationDuration);

	void setRotation(Vector3f leftRotation, int interpolationDuration);
}

