package com.myudog.myulib.api.game.object;

import com.myudog.myulib.api.game.bootstrap.GameObjectConfig;
import com.myudog.myulib.api.game.instance.GameInstance;
import net.minecraft.resources.Identifier;

import java.util.Map;

public record GameObjectContext(
    GameInstance<?> instance,
    Identifier objectId,
    GameObjectConfig config,
    Object runtimeObject,
    Identifier sourceEntityId,
    GameObjectKind interactionKind,
    Map<String, String> payload
) {
    public GameObjectContext {
        payload = payload == null ? Map.of() : Map.copyOf(payload);
    }
}



