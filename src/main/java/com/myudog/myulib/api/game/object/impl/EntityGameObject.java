package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.core.GameManager;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.object.behavior.IEntityBehavior;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * 實體類遊戲物件基礎實作。
 * 負責處理 Minecraft 實體的生命週期、動態座標追蹤以及與房間事件的掛載。
 * @param <E> 具體的 Minecraft 實體型別
 */
public abstract class EntityGameObject<E extends Entity> extends BaseGameObject {
    protected E entity;
    private final List<IEntityBehavior> behaviors = new ArrayList<>();

    protected EntityGameObject(Identifier id, GameObjectKind kind) {
        super(id, kind);
    }

    @Override
    public void onInitialize(GameInstance<?, ?, ?> instance) {
        for (IEntityBehavior behavior : behaviors) {
            behavior.onInitialize(this, instance);
        }
    }

    /**
     * 🌟 動態座標追蹤：
     * 若實體存活且已生成，回傳實體即時位置；若尚未生成，回傳配置中的 POS。
     * 確保所有相對座標判斷（如範圍偵測）皆以該生物當前位置為基準。
     */
    @Override
    public Vec3 getPosition() {
        if (entity != null && entity.isAlive()) {
            return entity.position();
        }
        return super.getPosition();
    }

    /**
     * 🌟 實體生成邏輯 (Template Method):
     * 由 BaseGameObject.spawn() 確保狀態為 NOT_LOADED 時才觸發。
     */
    @Override
    protected void onSpawn(GameInstance<?, ?, ?> instance) {
        summon(instance);
    }

    /**
     * 🌟 具體召喚邏輯：
     * 可供 onSpawn 呼叫，也可供子類在事件監聽器（如 EntityDeathEvent）中呼叫以實現「重新召喚」。
     */
    public void summon(GameInstance<?, ?, ?> instance) {
        // 清理舊有的殘留實體（若有）
        if (this.entity != null && this.entity.isAlive()) {
            GameManager.unregisterEntity(this.entity.getUUID());
            this.entity.discard();
        }

        Vec3 spawnPos = get(POS); // 取得強型別座標
        this.entity = createEntity(instance);

        if (this.entity != null) {
            this.entity.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            instance.getLevel().addFreshEntity(this.entity);
            GameManager.registerEntity(this.entity.getUUID(), instance.getInstanceId());
        }
    }

    /**
     * 🌟 資源清理邏輯 (Template Method):
     * 由 BaseGameObject.onDestroy() 確保狀態為 SPAWNED 時才觸發。
     */
    @Override
    protected void onDestroy(GameInstance<?, ?, ?> instance) {
        for (IEntityBehavior behavior : behaviors) {
            behavior.onDestroy(this, instance);
        }

        if (this.entity != null && this.entity.isAlive()) {
            GameManager.unregisterEntity(this.entity.getUUID());
            this.entity.discard();
        }
        this.entity = null;
    }

    /**
     * 由子類實作：建立具體的 Minecraft 實體。
     * 範例：return EntityType.ZOMBIE.create(instance.getWorld());
     */
    protected abstract E createEntity(GameInstance<?, ?, ?> instance);

    public E getEntity() {
        return entity;
    }

    public void addBehavior(IEntityBehavior behavior) {
        this.behaviors.add(behavior);
    }

    public void removeBehavior(IEntityBehavior behavior) {
        this.behaviors.remove(behavior);
    }
}