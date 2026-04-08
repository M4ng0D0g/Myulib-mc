package com.myudog.myulib.internal.game.timer;

import com.myudog.myulib.api.game.timer.TimerManager;
import com.myudog.myulib.api.game.timer.TimerModels;
import net.minecraft.resources.Identifier;

public class TimerRegistry {
    public static void register(TimerModels.Timer timer) { TimerManager.register(timer); }
    public static int createInstance(Identifier timerId, Long ownerEntityId, TimerModels.TimerPayload payload, boolean autoStart, Object Level) { return TimerManager.createInstance(timerId, ownerEntityId, payload, autoStart, Level); }
}
