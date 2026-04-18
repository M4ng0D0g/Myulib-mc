package com.myudog.myulib.api.game.object;

public enum GameObjectKind {
    LOGIC, // 邏輯重生點
    MINEABLE,
    ATTACKABLE,
    INTERACTABLE, // 可互動對象 (支援實體、方塊、範圍感應)
    PROXIMITY_TRIGGER,
    DECORATIVE,
    CUSTOM
}

