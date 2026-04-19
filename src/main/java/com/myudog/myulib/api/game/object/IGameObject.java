package com.myudog.myulib.api.game.object;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.core.Property;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Optional;

public interface IGameObject {

    Identifier getId(); // ID
    GameObjectKind getKind();

    Collection<Property<?>> getProperties();
    Optional<Property<?>> getProperty(String name);

    default boolean validate() {
        return true;
    }

    Vec3 getPosition();
    void setPosition(Vec3 pos);

    /** 遊戲啟動時初始化並綁定事件 */
    void onInitialize(GameInstance<?, ?, ?> instance);

    /** 執行物理生成 (實體生成或方塊放置) */
    void spawn(GameInstance<?, ?, ?> instance);

    /** 資源清理 */
    void destroy(GameInstance<?, ?, ?> instance);

    /**
     * 從設定模板建立一份可在單局遊戲中使用的物件副本。
     */
    IGameObject copy();
}