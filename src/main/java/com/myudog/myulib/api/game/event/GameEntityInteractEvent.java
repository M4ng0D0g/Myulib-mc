package com.myudog.myulib.api.game.event;

import com.myudog.myulib.api.event.Event;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

public final class GameEntityInteractEvent implements Event {
    private final ServerPlayer player;
    private final Entity target;
    private final InteractionHand hand;
    private boolean canceled;

    public GameEntityInteractEvent(ServerPlayer player, Entity target, InteractionHand hand) {
        this.player = player;
        this.target = target;
        this.hand = hand;
        this.canceled = false;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public Entity getTarget() {
        return target;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}

