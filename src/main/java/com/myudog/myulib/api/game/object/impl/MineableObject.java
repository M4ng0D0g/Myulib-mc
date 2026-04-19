package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.core.Property;
import com.myudog.myulib.api.game.object.IGameObject;
import com.myudog.myulib.api.game.object.behavior.MineableBehavior;
import net.minecraft.resources.Identifier;

/**
 * 可挖掘的資源物件 (如副本礦物、任務採集點)。
 * 繼承 BlockGameObject，具備自動對齊網格與遊戲結束環境還原的能力。
 */
public class MineableObject extends BlockGameObject {

    // 🌟 擴充自訂屬性：挖掘獎勵經驗值 (你可以自行替換為 ECS 資源組件)
    public static final Property<Integer> REWARD_EXP = new Property<>(
            "reward_exp",
            Integer.class,
            Integer::parseInt
    );

    public MineableObject(Identifier id) {
        super(id, GameObjectKind.MINEABLE);
        addBehavior(new MineableBehavior());
    }

    @Override
    protected void registerProperties() {
        super.registerProperties(); // 必須呼叫，以保留父類的 BLOCK_STATE 屬性
        define(REWARD_EXP, 10);     // 預設給予 10 點經驗
    }

    @Override
    public IGameObject copy() {
        MineableObject clone = new MineableObject(this.id);
        copyBaseStateTo(clone);
        return clone;
    }

}