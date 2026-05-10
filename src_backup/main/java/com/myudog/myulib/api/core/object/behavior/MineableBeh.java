package com.myudog.myulib.api.core.object.behavior;

import com.myudog.myulib.api.core.event.IEventListener;
import com.myudog.myulib.api.core.event.ProcessResult;
import com.myudog.myulib.api.framework.game.core.GameInstance;
import com.myudog.myulib.api.core.object.event.BlockBreakEvent;
import com.myudog.myulib.api.core.object.event.ObjectMineEvent;
import com.myudog.myulib.api.core.object.IObjectRt;

public final class MineableBeh implements IBlockBeh {
    private IEventListener<BlockBreakEvent> listener;

    @Override
    public void onInitialize(IObjectRt object, GameInstance<?, ?, ?> instance) {
        listener = event -> {
            if (event.getLevel() != instance.getLevel()) {
                return event.isCanceled() ? ProcessResult.CONSUME : ProcessResult.PASS;
            }

            var objectPos = object.getPosition();
            if (objectPos != null
                    && (int) objectPos.x == event.getPos().getX()
                    && (int) objectPos.y == event.getPos().getY()
                    && (int) objectPos.z == event.getPos().getZ()) {
                ObjectMineEvent objectMineEvent = new ObjectMineEvent(object, event.getPlayer());
                instance.getEventBus().dispatch(objectMineEvent);
                event.setCanceled(true);
                return ProcessResult.CONSUME;
            }

            return ProcessResult.PASS;
        };

        instance.getEventBus().subscribe(BlockBreakEvent.class, listener);
    }

    @Override
    public void onDestroy(IObjectRt object, GameInstance<?, ?, ?> instance) {
        if (listener != null) {
            instance.getEventBus().unsubscribe(BlockBreakEvent.class, listener);
            listener = null;
        }
    }
}



