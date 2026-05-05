package com.myudog.myulib.api.object.behavior;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.object.IObjectRt;

public interface IObjectBeh<T extends IObjectRt> {
    void onInitialize(T object, GameInstance<?, ?, ?> instance);

    // Required cleanup hook so each behavior can unsubscribe and release resources.
    void onDestroy(T object, GameInstance<?, ?, ?> instance);
}

