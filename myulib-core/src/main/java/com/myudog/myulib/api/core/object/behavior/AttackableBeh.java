package com.myudog.myulib.api.core.object.behavior;

import com.myudog.myulib.api.core.event.IEventListener;
import com.myudog.myulib.api.core.event.ProcessResult;
import com.myudog.myulib.api.core.object.IObjectRt;
import com.myudog.myulib.api.core.object.ObjectManager;
import com.myudog.myulib.api.core.object.event.EntityDamageEvent;
import com.myudog.myulib.api.core.object.event.EntityDeathEvent;
import com.myudog.myulib.api.core.object.event.ObjectDamageEvent;
import com.myudog.myulib.api.core.object.event.ObjectDeathEvent;

/**
 * Behavior that makes an entity object respond to damage and death events.
 * Subscribes to the global ObjectManager native event bus.
 */
public final class AttackableBeh implements IEntityBeh {
    private IEventListener<EntityDamageEvent> damageListener;
    private IEventListener<EntityDeathEvent> deathListener;

    @Override
    public void onInitialize(IObjectRt object) {
        var bus = ObjectManager.INSTANCE.getNativeEventBus();

        damageListener = event -> {
            var objectPos = object.getPosition();
            var victimPos = event.getVictim().blockPosition();
            if (objectPos == null
                    || (int) objectPos.x != victimPos.getX()
                    || (int) objectPos.y != victimPos.getY()
                    || (int) objectPos.z != victimPos.getZ()) {
                return ProcessResult.PASS;
            }
            ObjectDamageEvent objectDamageEvent = new ObjectDamageEvent(object, event.getSource(), event.getAmount());
            bus.dispatch(objectDamageEvent);
            if (objectDamageEvent.isCanceled()) {
                event.setCanceled(true);
            }
            return ProcessResult.CONSUME;
        };

        deathListener = event -> {
            var objectPos = object.getPosition();
            var victimPos = event.victim().blockPosition();
            if (objectPos == null
                    || (int) objectPos.x != victimPos.getX()
                    || (int) objectPos.y != victimPos.getY()
                    || (int) objectPos.z != victimPos.getZ()) {
                return ProcessResult.PASS;
            }
            bus.dispatch(new ObjectDeathEvent(object, event.source()));
            return ProcessResult.CONSUME;
        };

        bus.subscribe(EntityDamageEvent.class, damageListener);
        bus.subscribe(EntityDeathEvent.class, deathListener);
    }

    @Override
    public void onDestroy(IObjectRt object) {
        var bus = ObjectManager.INSTANCE.getNativeEventBus();
        if (damageListener != null) {
            bus.unsubscribe(EntityDamageEvent.class, damageListener);
            damageListener = null;
        }
        if (deathListener != null) {
            bus.unsubscribe(EntityDeathEvent.class, deathListener);
            deathListener = null;
        }
    }
}
