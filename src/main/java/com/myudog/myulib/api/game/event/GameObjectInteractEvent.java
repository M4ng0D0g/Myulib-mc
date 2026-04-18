package com.myudog.myulib.api.game.event;

import com.myudog.myulib.api.event.Event;
import com.myudog.myulib.api.game.object.IGameObject;
import net.minecraft.server.level.ServerPlayer;

public record GameObjectInteractEvent(IGameObject target, ServerPlayer player) implements Event {}
