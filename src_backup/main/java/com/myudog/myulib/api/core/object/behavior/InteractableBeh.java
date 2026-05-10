package com.myudog.myulib.api.core.object.behavior;

import com.myudog.myulib.api.core.event.ProcessResult;
import com.myudog.myulib.api.core.event.IEventListener;
import com.myudog.myulib.api.framework.game.core.GameInstance;
import com.myudog.myulib.api.core.object.event.BlockInteractEvent;
import com.myudog.myulib.api.core.object.event.EntityInteractEvent;
import com.myudog.myulib.api.core.object.event.ObjectInteractEvent;
import com.myudog.myulib.api.core.object.IObjectRt;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InteractableBeh implements IBlockBeh {
    private final Map<UUID, Long> lastInteractTime = new ConcurrentHashMap<>();
    private IEventListener<BlockInteractEvent> listener;
    private IEventListener<EntityInteractEvent> entityListener;

    @Override
    public void onInitialize(IObjectRt object, GameInstance<?, ?, ?> instance) {
        listener = event -> {
            if (event.getLevel() != instance.getLevel()) {
                return event.isCanceled() ? ProcessResult.CONSUME : ProcessResult.PASS;
            }

            var objectPos = object.getPosition();
            if (objectPos == null
                    || (int) objectPos.x != event.getPos().getX()
                    || (int) objectPos.y != event.getPos().getY()
                    || (int) objectPos.z != event.getPos().getZ()) {
                return ProcessResult.PASS;
            }

            long now = System.currentTimeMillis();
            long lastTime = lastInteractTime.getOrDefault(event.getPlayer().getUUID(), 0L);
            long cooldown = 0L;
            if (now - lastTime < cooldown) {
                return ProcessResult.CONSUME;
            }

            lastInteractTime.put(event.getPlayer().getUUID(), now);
            ObjectInteractEvent objectInteractEvent = new ObjectInteractEvent(object, event.getPlayer());
            instance.getEventBus().dispatch(objectInteractEvent);
            event.setCanceled(true);
            return ProcessResult.CONSUME;
        };

        instance.getEventBus().subscribe(BlockInteractEvent.class, listener);

        entityListener = event -> {
            if (event.getTarget().level() != instance.getLevel()) {
                return event.isCanceled() ? ProcessResult.CONSUME : ProcessResult.PASS;
            }

            var objectPos = object.getPosition();
            var targetPos = event.getTarget().blockPosition();
            if (objectPos == null
                    || (int) objectPos.x != targetPos.getX()
                    || (int) objectPos.y != targetPos.getY()
                    || (int) objectPos.z != targetPos.getZ()) {
                return ProcessResult.PASS;
            }

            long now = System.currentTimeMillis();
            long lastTime = lastInteractTime.getOrDefault(event.getPlayer().getUUID(), 0L);
            long cooldown = 0L;
            if (now - lastTime < cooldown) {
                return ProcessResult.CONSUME;
            }

            lastInteractTime.put(event.getPlayer().getUUID(), now);
            ObjectInteractEvent objectInteractEvent = new ObjectInteractEvent(object, event.getPlayer());
            instance.getEventBus().dispatch(objectInteractEvent);
            event.setCanceled(true);
            return ProcessResult.CONSUME;
        };

        instance.getEventBus().subscribe(EntityInteractEvent.class, entityListener);
    }

    @Override
    public void onDestroy(IObjectRt object, GameInstance<?, ?, ?> instance) {
        if (listener != null) {
            instance.getEventBus().unsubscribe(BlockInteractEvent.class, listener);
            listener = null;
        }
        if (entityListener != null) {
            instance.getEventBus().unsubscribe(EntityInteractEvent.class, entityListener);
            entityListener = null;
        }
        lastInteractTime.clear();
    }
}


