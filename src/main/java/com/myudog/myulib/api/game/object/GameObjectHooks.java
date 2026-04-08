package com.myudog.myulib.api.game.object;

import com.myudog.myulib.api.game.bootstrap.GameObjectConfig;
import com.myudog.myulib.api.game.GameManager;
import com.myudog.myulib.api.game.instance.GameInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;

import java.util.Map;

public final class GameObjectHooks {
    private GameObjectHooks() {
    }

    public static GameObjectDefinition register(GameInstance<?> instance, GameObjectConfig config, Object runtimeObject) {
        return instance == null ? null : instance.objectBindings().register(instance, config, runtimeObject);
    }

    public static void attach(GameInstance<?> instance, Identifier objectId, Object runtimeObject) {
        if (instance != null) {
            instance.objectBindings().bindRuntime(instance, objectId, runtimeObject);
        }
    }

    public static void detach(GameInstance<?> instance) {
        if (instance != null) {
            instance.objectBindings().clear(instance);
        }
    }

    public static void tick(GameInstance<?> instance) {
        if (instance != null) {
            instance.objectBindings().tick(instance);
        }
    }

    public static void tickAll() {
        for (GameInstance<?> instance : GameManager.getInstances()) {
            tick(instance);
        }
    }

    public static boolean interact(GameInstance<?> instance, Identifier objectId, Identifier sourceEntityId, GameObjectKind kind, Map<String, String> payload) {
        return instance != null && instance.objectBindings().interact(instance, objectId, sourceEntityId, kind, payload);
    }

    public static boolean interactByKindAndType(GameObjectKind kind, Identifier type, Identifier sourceEntityId, Map<String, String> payload) {
        boolean consumed = false;
        for (GameInstance<?> instance : GameManager.getInstances()) {
            consumed |= instance.objectBindings().interactByKindAndType(instance, kind, type, sourceEntityId, payload);
        }
        return consumed;
    }

    public static boolean interactByKindsAndType(Identifier type, Identifier sourceEntityId, Map<String, String> payload, GameObjectKind... kinds) {
        boolean consumed = false;
        if (kinds == null) {
            return false;
        }
        for (GameObjectKind kind : kinds) {
            consumed |= interactByKindAndType(kind, type, sourceEntityId, payload);
        }
        return consumed;
    }

    public static Identifier toPlayerIdentifier(ServerPlayer player) {
        if (player == null) {
            return null;
        }
        return Identifier.fromNamespaceAndPath("myulib", "player_" + player.getUUID().toString().replace("-", ""));
    }
}




