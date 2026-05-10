package com.myudog.myulib.api.core.object.behavior;

import com.myudog.myulib.api.core.event.IEventListener;
import com.myudog.myulib.api.core.event.ProcessResult;
import com.myudog.myulib.api.framework.game.core.GameInstance;
import com.myudog.myulib.api.core.object.IObjectRt;
import com.myudog.myulib.api.core.object.event.EntityDamageEvent;
import com.myudog.myulib.api.core.object.event.EntityDeathEvent;
import com.myudog.myulib.api.core.object.event.ObjectDamageEvent;
import com.myudog.myulib.api.core.object.event.ObjectDeathEvent;

public final class AttackableBeh implements IEntityBeh {
    private IEventListener<EntityDamageEvent> damageListener;
    private IEventListener<EntityDeathEvent> deathListener;

    @Override
    public void onInitialize(IObjectRt object, GameInstance<?, ?, ?> instance) {
        damageListener = event -> {
            if (event.getVictim().level() != instance.getLevel()) {
                return ProcessResult.PASS;
            }

            var objectPos = object.getPosition();
            var victimPos = event.getVictim().blockPosition();
            if (objectPos == null
                    || (int) objectPos.x != victimPos.getX()
                    || (int) objectPos.y != victimPos.getY()
                    || (int) objectPos.z != victimPos.getZ()) {
                return ProcessResult.PASS;
            }

            ObjectDamageEvent objectDamageEvent = new ObjectDamageEvent(object, event.getSource(), event.getAmount());
            instance.getEventBus().dispatch(objectDamageEvent);
            if (objectDamageEvent.isCanceled()) {
                event.setCanceled(true);
            }
            return ProcessResult.CONSUME;
        };

        deathListener = event -> {
            if (event.victim().level() != instance.getLevel()) {
                return ProcessResult.PASS;
            }

            var objectPos = object.getPosition();
            var victimPos = event.victim().blockPosition();
            if (objectPos == null
                    || (int) objectPos.x != victimPos.getX()
                    || (int) objectPos.y != victimPos.getY()
                    || (int) objectPos.z != victimPos.getZ()) {
                return ProcessResult.PASS;
            }

            instance.getEventBus().dispatch(new ObjectDeathEvent(object, event.source()));
            return ProcessResult.CONSUME;
        };

        instance.getEventBus().subscribe(EntityDamageEvent.class, damageListener);
        instance.getEventBus().subscribe(EntityDeathEvent.class, deathListener);
    }

    @Override
    public void onDestroy(IObjectRt object, GameInstance<?, ?, ?> instance) {
        if (damageListener != null) {
            instance.getEventBus().unsubscribe(EntityDamageEvent.class, damageListener);
            damageListener = null;
        }
        if (deathListener != null) {
            instance.getEventBus().unsubscribe(EntityDeathEvent.class, deathListener);
            deathListener = null;
        }
    }
}



