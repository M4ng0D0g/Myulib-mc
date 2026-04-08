package com.myudog.myulib.internal.entity;

import com.myudog.myulib.api.floating.IFloatingObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;import org.joml.Vector3f;



public class ItemDisplayObject implements IFloatingObject {
	private final ServerLevel Level;
	private final ItemStack itemStack;
	private ItemDisplay entity;
	private Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
	private Vector3f rotation = new Vector3f();

	public ItemDisplayObject(ServerLevel Level, ItemStack itemStack) {
		this.Level = Level;
		this.itemStack = itemStack;
	}

	@Override
	public void spawn(Vec3 pos) {
		ItemDisplay display = new ItemDisplay(EntityType.ITEM_DISPLAY, Level);
		display.setItemStack(itemStack.copy());
		display.setPos(pos);
		Level.addFreshEntity(display);
		entity = display;
	}

	@Override
	public void remove() {
		if (entity != null) {
			entity.discard();
			entity = null;
		}
	}

	@Override
	public void moveTo(Vec3 pos, int interpolationDuration) {
		if (entity != null) {
			entity.setPos(pos);
		}
	}

	@Override
	public void setScale(Vector3f scale, int interpolationDuration) {
		this.scale = new Vector3f(scale);
	}

	@Override
	public void setRotation(Vector3f leftRotation, int interpolationDuration) {
		this.rotation = new Vector3f(leftRotation);
	}
}


