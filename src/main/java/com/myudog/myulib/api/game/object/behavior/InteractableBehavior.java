package com.myudog.myulib.api.game.object.behavior;

import com.myudog.myulib.api.event.ProcessResult;
import com.myudog.myulib.api.event.listener.EventListener;
import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.event.GameBlockInteractEvent;
import com.myudog.myulib.api.game.event.GameObjectInteractEvent;
import com.myudog.myulib.api.game.object.impl.BlockGameObject;
import com.myudog.myulib.api.game.object.impl.InteractableObject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InteractableBehavior implements IBlockBehavior {
    private final Map<UUID, Long> lastInteractTime = new ConcurrentHashMap<>();
    private EventListener<GameBlockInteractEvent> listener;

    @Override
    public void onInitialize(BlockGameObject object, GameInstance<?, ?, ?> instance) {
        if (!(object instanceof InteractableObject interactableObject)) {
            throw new IllegalArgumentException("InteractableBehavior can only be attached to InteractableObject");
        }

        listener = event -> {
            if (event.getLevel() != instance.getLevel()) {
                return event.isCanceled() ? ProcessResult.CONSUME : ProcessResult.PASS;
            }

            if (!object.containsPos(event.getPos())) {
                return ProcessResult.PASS;
            }

            long now = System.currentTimeMillis();
            long lastTime = lastInteractTime.getOrDefault(event.getPlayer().getUUID(), 0L);
            long cooldown = interactableObject.get(InteractableObject.COOLDOWN_MS);
            if (now - lastTime < cooldown) {
                return ProcessResult.CONSUME;
            }

            lastInteractTime.put(event.getPlayer().getUUID(), now);
            event.setCanceled(true);
            instance.getEventBus().dispatch(new GameObjectInteractEvent(object, event.getPlayer()));
            return ProcessResult.CONSUME;
        };

        instance.getEventBus().subscribe(GameBlockInteractEvent.class, listener);
    }

    @Override
    public void onDestroy(BlockGameObject object, GameInstance<?, ?, ?> instance) {
        if (listener != null) {
            instance.getEventBus().unsubscribe(GameBlockInteractEvent.class, listener);
            listener = null;
        }
        lastInteractTime.clear();
    }
}


