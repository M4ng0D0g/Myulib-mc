package com.myudog.myulib.api.core.object.event;

import com.myudog.myulib.api.core.event.IEvent;
import com.myudog.myulib.api.core.object.IObjectRt;
import net.minecraft.server.level.ServerPlayer;

public final class ObjectInteractEvent implements IEvent {
	private final IObjectRt target;
	private final ServerPlayer player;
	private boolean canceled;

	public ObjectInteractEvent(IObjectRt target, ServerPlayer player) {
		this.target = target;
		this.player = player;
		this.canceled = false;
	}

	public IObjectRt getTarget() {
		return target;
	}

	public ServerPlayer getPlayer() {
		return player;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
