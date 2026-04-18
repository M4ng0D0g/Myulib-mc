package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.object.GameObjectProperty;
import com.myudog.myulib.api.game.object.IGameObject;
import net.minecraft.resources.Identifier;

/**
 * 玩家重生點物件。
 * 純邏輯物件，不進行物理生成，僅提供座標給房間重生系統使用。
 */
public class RespawnPointObject extends BaseGameObject {

    // 🌟 定義強型別屬性：所屬隊伍 ID
    public static final GameObjectProperty<String> TEAM_ID = new GameObjectProperty<>(
            "team_id",
            String.class,
            s -> s
    );

    public RespawnPointObject(Identifier id) {
        super(id, GameObjectKind.LOGIC); // 標示為純邏輯物件
    }

    @Override
    protected void registerProperties() {
        define(TEAM_ID, "spectator"); // 預設為觀察者隊伍
    }

    /**
     * 驗證邏輯：確保有座標即可。
     */
    @Override
    public boolean validate() {
        return get(POS) != null;
    }

    @Override
    public void onInitialize(GameInstance<?, ?, ?> instance) {
        // 🌟 自動綁定：向房間的 Data 註冊此重生點
        // 這樣在玩家死亡/加入房間時，邏輯可以直接從 GameData 查到這個物件並獲取 POS 與 LEVEL
        /* String team = get(TEAM_ID);
        instance.getData().registerRespawnPoint(team, this);
        */
    }

    @Override
    protected void onSpawn(GameInstance<?, ?, ?> instance) {
        // [EMPTY] 邏輯物件不需物理生成
    }

    @Override
    protected void onDestroy(GameInstance<?, ?, ?> instance) {
        // 🌟 遊戲結束時清理註冊，防止記憶體洩漏
        /*
        String team = get(TEAM_ID);
        instance.getData().unregisterRespawnPoint(team, this);
        */
    }

    @Override
    public IGameObject copy() {
        RespawnPointObject clone = new RespawnPointObject(this.id);
        copyBaseStateTo(clone);
        return clone;
    }
}