package com.myudog.myulib.api.game.object.behavior;

import com.myudog.myulib.api.event.listener.EventListener;
import com.myudog.myulib.api.event.ProcessResult;
import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.event.GameBlockBreakEvent;
import com.myudog.myulib.api.game.event.GameObjectMineEvent;
import com.myudog.myulib.api.game.object.impl.BlockGameObject;

public final class MineableBehavior implements IBlockBehavior {
    private EventListener<GameBlockBreakEvent> listener;

    @Override
    public void onInitialize(BlockGameObject object, GameInstance<?, ?, ?> instance) {
        listener = event -> {
            if (event.getLevel() != instance.getLevel()) {
                return event.isCanceled() ? ProcessResult.CONSUME : ProcessResult.PASS;
            }

            if (object.containsPos(event.getPos())) {
                event.setCanceled(true);
                instance.getEventBus().dispatch(new GameObjectMineEvent(object, event.getPlayer()));
                return ProcessResult.CONSUME;
            }

            return ProcessResult.PASS;
        };

        instance.getEventBus().subscribe(GameBlockBreakEvent.class, listener);
    }

    @Override
    public void onDestroy(BlockGameObject object, GameInstance<?, ?, ?> instance) {
        if (listener != null) {
            instance.getEventBus().unsubscribe(GameBlockBreakEvent.class, listener);
            listener = null;
        }
    }
}



