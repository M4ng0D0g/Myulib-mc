package com.myudog.myulib.api.core.object.event;

import com.myudog.myulib.api.core.event.IEvent;
import com.myudog.myulib.api.core.object.IObjectRt;
import net.minecraft.world.damagesource.DamageSource;

public final class ObjectDamageEvent implements IEvent {
	private final IObjectRt target;
	private final DamageSource source;
	private final float amount;
	private boolean canceled;

	public ObjectDamageEvent(IObjectRt target, DamageSource source, float amount) {
		this.target = target;
		this.source = source;
		this.amount = amount;
		this.canceled = false;
	}

	public IObjectRt getTarget() {
		return target;
	}

	public DamageSource getSource() {
		return source;
	}

	public float getAmount() {
		return amount;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
