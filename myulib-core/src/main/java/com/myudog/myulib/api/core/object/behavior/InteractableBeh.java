package com.myudog.myulib.api.core.object.behavior;

import com.myudog.myulib.api.core.event.ProcessResult;
import com.myudog.myulib.api.core.event.IEventListener;
import com.myudog.myulib.api.core.object.IObjectRt;
import com.myudog.myulib.api.core.object.ObjectManager;
import com.myudog.myulib.api.core.object.event.BlockInteractEvent;
import com.myudog.myulib.api.core.object.event.EntityInteractEvent;
import com.myudog.myulib.api.core.object.event.ObjectInteractEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Behavior that makes an object (block or entity) respond to interaction events.
 * Subscribes to the global ObjectManager native event bus.
 */
public final class InteractableBeh implements IBlockBeh {
    private final Map<UUID, Long> lastInteractTime = new ConcurrentHashMap<>();
    private IEventListener<BlockInteractEvent> listener;
    private IEventListener<EntityInteractEvent> entityListener;

    @Override
    public void onInitialize(IObjectRt object) {
        var bus = ObjectManager.INSTANCE.getNativeEventBus();

        listener = event -> {
            var objectPos = object.getPosition();
            if (objectPos == null
                    || (int) objectPos.x != event.getPos().getX()
                    || (int) objectPos.y != event.getPos().getY()
                    || (int) objectPos.z != event.getPos().getZ()) {
                return ProcessResult.PASS;
            }
            long now = System.currentTimeMillis();
            if (now - lastInteractTime.getOrDefault(event.getPlayer().getUUID(), 0L) < 0L) {
                return ProcessResult.CONSUME;
            }
            lastInteractTime.put(event.getPlayer().getUUID(), now);
            ObjectInteractEvent objectInteractEvent = new ObjectInteractEvent(object, event.getPlayer());
            bus.dispatch(objectInteractEvent);
            event.setCanceled(true);
            return ProcessResult.CONSUME;
        };

        entityListener = event -> {
            var objectPos = object.getPosition();
            var targetPos = event.getTarget().blockPosition();
            if (objectPos == null
                    || (int) objectPos.x != targetPos.getX()
                    || (int) objectPos.y != targetPos.getY()
                    || (int) objectPos.z != targetPos.getZ()) {
                return ProcessResult.PASS;
            }
            long now = System.currentTimeMillis();
            if (now - lastInteractTime.getOrDefault(event.getPlayer().getUUID(), 0L) < 0L) {
                return ProcessResult.CONSUME;
            }
            lastInteractTime.put(event.getPlayer().getUUID(), now);
            ObjectInteractEvent objectInteractEvent = new ObjectInteractEvent(object, event.getPlayer());
            bus.dispatch(objectInteractEvent);
            event.setCanceled(true);
            return ProcessResult.CONSUME;
        };

        bus.subscribe(BlockInteractEvent.class, listener);
        bus.subscribe(EntityInteractEvent.class, entityListener);
    }

    @Override
    public void onDestroy(IObjectRt object) {
        var bus = ObjectManager.INSTANCE.getNativeEventBus();
        if (listener != null) {
            bus.unsubscribe(BlockInteractEvent.class, listener);
            listener = null;
        }
        if (entityListener != null) {
            bus.unsubscribe(EntityInteractEvent.class, entityListener);
            entityListener = null;
        }
        lastInteractTime.clear();
    }
}
