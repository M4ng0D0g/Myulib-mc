package com.myudog.myulib.api.core.ecs.lifecycle;

import com.myudog.myulib.api.core.ecs.IComponent;

public interface Resettable extends IComponent {
    void reset();
}

