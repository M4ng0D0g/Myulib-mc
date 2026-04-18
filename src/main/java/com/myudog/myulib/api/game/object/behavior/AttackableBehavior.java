package com.myudog.myulib.api.game.object.behavior;

import com.myudog.myulib.api.event.ProcessResult;
import com.myudog.myulib.api.event.listener.EventListener;
import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.event.GameEntityDamageEvent;
import com.myudog.myulib.api.game.event.GameEntityDeathEvent;
import com.myudog.myulib.api.game.event.GameObjectDamageEvent;
import com.myudog.myulib.api.game.event.GameObjectDeathEvent;
import com.myudog.myulib.api.game.object.impl.EntityGameObject;
import net.minecraft.world.entity.Entity;

public final class AttackableBehavior implements IEntityBehavior {
    private EventListener<GameEntityDamageEvent> damageListener;
    private EventListener<GameEntityDeathEvent> deathListener;

    @Override
    public void onInitialize(EntityGameObject<? extends Entity> object, GameInstance<?, ?, ?> instance) {
        damageListener = event -> {
            Entity entity = object.getEntity();
            if (entity == null) {
                return ProcessResult.PASS;
            }

            if (event.victim().getUUID().equals(entity.getUUID())) {
                instance.getEventBus().dispatch(new GameObjectDamageEvent(object, event.source(), event.amount()));
                return ProcessResult.CONSUME;
            }
            return ProcessResult.PASS;
        };

        deathListener = event -> {
            Entity entity = object.getEntity();
            if (entity == null) {
                return ProcessResult.PASS;
            }

            if (event.victim().getUUID().equals(entity.getUUID())) {
                instance.getEventBus().dispatch(new GameObjectDeathEvent(object, event.source()));
                return ProcessResult.CONSUME;
            }
            return ProcessResult.PASS;
        };

        instance.getEventBus().subscribe(GameEntityDamageEvent.class, damageListener);
        instance.getEventBus().subscribe(GameEntityDeathEvent.class, deathListener);
    }

    @Override
    public void onDestroy(EntityGameObject<? extends Entity> object, GameInstance<?, ?, ?> instance) {
        if (damageListener != null) {
            instance.getEventBus().unsubscribe(GameEntityDamageEvent.class, damageListener);
            damageListener = null;
        }
        if (deathListener != null) {
            instance.getEventBus().unsubscribe(GameEntityDeathEvent.class, deathListener);
            deathListener = null;
        }
    }
}

