package com.myudog.myulib.api.effect;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public final class SpatialEffectEvents {
    private SpatialEffectEvents() {
    }

    public static void register(ISpatialEffectManager effectManager) {
        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((player, origin, destination) ->
                effectManager.clearPlayer(player)
        );

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            effectManager.clearPlayer(oldPlayer);
            effectManager.clearPlayer(newPlayer);
        });

        ServerPlayerEvents.LEAVE.register(effectManager::clearPlayer);
    }
}



