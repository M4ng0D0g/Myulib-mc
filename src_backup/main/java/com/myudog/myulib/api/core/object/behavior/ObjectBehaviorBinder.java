package com.myudog.myulib.api.core.object.behavior;

import com.myudog.myulib.api.framework.game.core.GameInstance;
import com.myudog.myulib.api.core.object.IObjectRt;
import com.myudog.myulib.api.core.object.ObjectKind;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ObjectBehaviorBinder {
    private static final Map<IObjectRt, List<IObjectBeh<IObjectRt>>> ATTACHED = new ConcurrentHashMap<>();

    private ObjectBehaviorBinder() {
    }

    public static void attach(IObjectRt object, GameInstance<?, ?, ?> instance) {
        if (object == null || instance == null) return;
        List<IObjectBeh<IObjectRt>> behaviors = resolve(object.getKind());
        List<IObjectBeh<IObjectRt>> existing = ATTACHED.putIfAbsent(object, behaviors);
        if (existing != null) return;

        behaviors.forEach(behavior -> behavior.onInitialize(object, instance));
    }

    public static void detach(IObjectRt object, GameInstance<?, ?, ?> instance) {
        if (object == null || instance == null) return;
        List<IObjectBeh<IObjectRt>> behaviors = ATTACHED.remove(object);
        if (behaviors == null) return;
        for (IObjectBeh<IObjectRt> behavior : behaviors) {
            behavior.onDestroy(object, instance);
        }
    }

    private static List<IObjectBeh<IObjectRt>> resolve(ObjectKind kind) {
        if (kind == null) {
            return List.of();
        }

        return switch (kind) {
            case MINEABLE -> List.of(new MineableBeh());
            case INTERACTABLE -> List.of(new InteractableBeh());
            case ATTACKABLE -> List.of(new AttackableBeh());
            default -> List.of();
        };
    }
}

