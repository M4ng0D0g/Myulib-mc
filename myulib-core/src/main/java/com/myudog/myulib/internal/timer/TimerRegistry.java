package com.myudog.myulib.internal.timer;

import com.myudog.myulib.api.core.timer.TimerDefinition;
import com.myudog.myulib.api.core.timer.TimerManager;
import com.myudog.myulib.api.core.timer.TimerPayload;
import net.minecraft.resources.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TimerRegistry {
    public static void register(TimerDefinition timer) { TimerManager.INSTANCE.register(timer); }
    public static UUID createInstance(Identifier timerId, Long ownerEntityId, TimerPayload payload, boolean autoStart) {
        UUID timerUuid = UUID.nameUUIDFromBytes(timerId.toString().getBytes(StandardCharsets.UTF_8));
        return TimerManager.INSTANCE.createInstance(timerUuid, ownerEntityId, payload, autoStart);
    }
}
