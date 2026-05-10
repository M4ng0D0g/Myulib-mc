package com.myudog.myulib.api.core.object;

public enum ObjectKind {
    LOGIC, // 邏輯重生點
    MINEABLE,
    ATTACKABLE,
    INTERACTABLE, // 可互動對象 (支援實體、方塊、範圍感應)
    PROXIMITY_TRIGGER,
    DECORATIVE,
    CUSTOM;

    public boolean isBlockKind() {
        return this == MINEABLE || this == INTERACTABLE;
    }

    public boolean isEntityKind() {
        return this == ATTACKABLE;
    }

    public boolean isLogicKind() {
        return this == LOGIC || this == PROXIMITY_TRIGGER;
    }

    public boolean supportsMixinInterception() {
        return isBlockKind() || isEntityKind();
    }
}

