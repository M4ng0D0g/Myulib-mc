package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.object.IGameObject;
import com.myudog.myulib.api.game.object.behavior.AttackableBehavior;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

public class AttackableObject<E extends Entity> extends EntityGameObject<E> {

    private final EntityType<E> entityType;

    public AttackableObject(Identifier id, EntityType<E> entityType) {
        super(id, GameObjectKind.ATTACKABLE);
        this.entityType = entityType;
        addBehavior(new AttackableBehavior());
    }

    @Override
    protected void registerProperties() {
        // 給予預設值
    }


    /**
     * 🌟 實作 EntityGameObject 的抽象方法。
     * 負責產生具體的 Minecraft 實體，座標與生成的處理會由父類別接手。
     */
    @Override
    protected E createEntity(GameInstance<?, ?, ?> instance) {
        return this.entityType.create(instance.getLevel(), EntitySpawnReason.COMMAND);
    }

    @Override
    public IGameObject copy() {
        AttackableObject<E> clone = new AttackableObject<>(this.id, this.entityType);
        copyBaseStateTo(clone);
        return clone;
    }
}