package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.object.GameObjectProperty;
import com.myudog.myulib.api.game.object.IGameObject;
import com.myudog.myulib.api.game.object.behavior.InteractableBehavior;
import net.minecraft.resources.Identifier;

/**
 * 右鍵互動型遊戲物件。
 * 當玩家對著指定的方塊或生物按下右鍵時觸發。
 */
public class InteractableObject extends BlockGameObject {

    // 🌟 定義強型別屬性：防止連點的冷卻時間 (毫秒)
    public static final GameObjectProperty<Long> COOLDOWN_MS = new GameObjectProperty<>(
            "cooldown_ms",
            Long.class,
            Long::parseLong
    );

    public InteractableObject(Identifier id) {
        super(id, GameObjectKind.INTERACTABLE);
        addBehavior(new InteractableBehavior());
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        define(COOLDOWN_MS, 500L); // 預設防連點冷卻 0.5 秒
    }

    @Override
    public boolean validate() {
        return get(POS) != null;
    }

    @Override
    public IGameObject copy() {
        InteractableObject clone = new InteractableObject(this.id);
        copyBaseStateTo(clone);
        return clone;
    }
}