package com.myudog.myulib.api.core.ecs.lifecycle;

import com.myudog.myulib.api.core.ecs.IComponent;

public interface DimensionAware extends IComponent {
    default DimensionChangePolicy getDimensionPolicy() {
        return DimensionChangePolicy.KEEP;
    }
}

