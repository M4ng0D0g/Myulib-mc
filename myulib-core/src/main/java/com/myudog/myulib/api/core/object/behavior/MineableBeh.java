package com.myudog.myulib.api.core.object.behavior;

import com.myudog.myulib.api.core.event.IEventListener;
import com.myudog.myulib.api.core.event.ProcessResult;
import com.myudog.myulib.api.core.object.IObjectRt;
import com.myudog.myulib.api.core.object.ObjectManager;
import com.myudog.myulib.api.core.object.event.BlockBreakEvent;
import com.myudog.myulib.api.core.object.event.ObjectMineEvent;

/**
 * Behavior that makes a block object respond to mining events.
 * Subscribes to the global ObjectManager native event bus.
 */
public final class MineableBeh implements IBlockBeh {
    private IEventListener<BlockBreakEvent> listener;

    @Override
    public void onInitialize(IObjectRt object) {
        var bus = ObjectManager.INSTANCE.getNativeEventBus();

        listener = event -> {
            var objectPos = object.getPosition();
            if (objectPos != null
                    && (int) objectPos.x == event.getPos().getX()
                    && (int) objectPos.y == event.getPos().getY()
                    && (int) objectPos.z == event.getPos().getZ()) {
                ObjectMineEvent objectMineEvent = new ObjectMineEvent(object, event.getPlayer());
                bus.dispatch(objectMineEvent);
                event.setCanceled(true);
                return ProcessResult.CONSUME;
            }
            return ProcessResult.PASS;
        };

        bus.subscribe(BlockBreakEvent.class, listener);
    }

    @Override
    public void onDestroy(IObjectRt object) {
        if (listener != null) {
            ObjectManager.INSTANCE.getNativeEventBus().unsubscribe(BlockBreakEvent.class, listener);
            listener = null;
        }
    }
}
