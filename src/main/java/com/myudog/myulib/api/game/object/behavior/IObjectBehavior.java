package com.myudog.myulib.api.game.object.behavior;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.object.impl.BaseGameObject;

public interface IObjectBehavior<T extends BaseGameObject> {
    void onInitialize(T object, GameInstance<?, ?, ?> instance);

    // Required cleanup hook so each behavior can unsubscribe and release resources.
    void onDestroy(T object, GameInstance<?, ?, ?> instance);
}

