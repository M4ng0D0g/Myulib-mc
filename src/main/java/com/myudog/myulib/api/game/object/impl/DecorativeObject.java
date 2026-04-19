package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.core.Property;
import com.myudog.myulib.api.game.object.IGameObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * 純裝飾型遊戲物件。
 * 專門用於生成 DisplayEntity (如 BlockDisplay, ItemDisplay, TextDisplay) 或 ArmorStand。
 * 完全不掛載任何遊戲事件，保持極致輕量。
 */
public class DecorativeObject<E extends Entity> extends EntityGameObject<E> {

    // 🌟 將實體類型鎖死在編譯期
    private final EntityType<E> entityType;

    // 🌟 定義 NBT 屬性，這對 DisplayEntity (展示實體) 非常重要！
    // 例如設定大小、顯示的方塊/物品、旋轉角度等，全部透過 NBT 驅動
    public static final Property<CompoundTag> NBT_DATA = new Property<>(
            "nbt",
            CompoundTag.class,
            s -> {
                try {
                    return TagParser.parseCompoundFully(s);
                } catch (Exception e) { return new CompoundTag(); }
            }
    );

    public DecorativeObject(Identifier id, EntityType<E> entityType) {
        super(id, GameObjectKind.DECORATIVE);
        this.entityType = entityType;
    }

    @Override
    protected void registerProperties() {
        define(NBT_DATA, new CompoundTag()); // 預設空 NBT
    }

    /**
     * 🌟 保持靜態：不訂閱任何事件，節省效能
     */
    @Override
    public void onInitialize(GameInstance<?, ?, ?> instance) {
        // [EMPTY]
    }

    /**
     * 🌟 透過父類的範本方法建立實體
     * 完全不需要覆寫 spawn() 和 onDestroy()，EntityGameObject 已經包辦了座標與生成
     */
    @Override
    @SuppressWarnings("unchecked")
    protected E createEntity(GameInstance<?, ?, ?> instance) {
        CompoundTag tag = get(NBT_DATA);

        // 如果設定檔有提供 NBT，直接利用 Minecraft 底層的資料驅動生成 API
        if (tag != null && !tag.isEmpty()) {
            ValueInput input = TagValueInput.create(
                    new ProblemReporter.ScopedCollector(LoggerFactory.getLogger("Myulib")),
                    instance.getLevel().registryAccess(),
                    tag
            );

            Optional<Entity> optEntity = EntityType.create(
                    this.entityType,
                    input,
                    instance.getLevel(),
                    EntitySpawnReason.COMMAND
            );

            return (E) optEntity.orElse(null);
        } else {
            // 沒有 NBT 的情況，生成純白板展示實體
            return this.entityType.create(instance.getLevel(), EntitySpawnReason.COMMAND);
        }
    }

    @Override
    public IGameObject copy() {
        DecorativeObject<E> clone = new DecorativeObject<>(this.id, this.entityType);
        copyBaseStateTo(clone);
        return clone;
    }
}