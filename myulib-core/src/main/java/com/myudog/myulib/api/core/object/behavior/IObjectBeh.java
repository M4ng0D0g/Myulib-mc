package com.myudog.myulib.api.core.object.behavior;

import com.myudog.myulib.api.core.object.IObjectRt;

public interface IObjectBeh<T extends IObjectRt> {
    void onInitialize(T object);

    // Required cleanup hook so each behavior can unsubscribe and release resources.
    void onDestroy(T object);
}

