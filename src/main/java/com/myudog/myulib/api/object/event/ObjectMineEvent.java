package com.myudog.myulib.api.object.event;

import com.myudog.myulib.api.core.event.IEvent;
import com.myudog.myulib.api.object.IObjectRt;
import net.minecraft.server.level.ServerPlayer;

public record ObjectMineEvent(IObjectRt target, ServerPlayer player) implements IEvent {}
