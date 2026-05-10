package com.myudog.myulib.api;

import com.myudog.myulib.api.core.floating.IFloatingObject;
import com.myudog.myulib.internal.entity.ItemDisplayObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;



public final class MyuVFX {
	private MyuVFX() {
	}

	public static IFloatingObject createItemObject(ServerLevel Level, ItemStack itemStack) {
		return new ItemDisplayObject(Level, itemStack);
	}
}


