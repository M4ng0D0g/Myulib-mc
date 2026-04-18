package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.object.GameObjectProperty;
import com.myudog.myulib.api.game.object.behavior.IBlockBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

/**
 * 方塊類遊戲物件。
 * 負責處理與 Minecraft 世界網格相關的物件（如礦物、祭壇、區域標記）。
 */
public abstract class BlockGameObject extends BaseGameObject {

    // 🌟 定義強型別屬性：該物件代表的方塊狀態
    public static final GameObjectProperty<BlockState> BLOCK_STATE = new GameObjectProperty<>(
            "block_state",
            BlockState.class,
            s -> Blocks.AIR.defaultBlockState() // 此處建議對接一個 BlockState 序列化器
    );

    // AABB 採絕對世界座標: minX,minY,minZ,maxX,maxY,maxZ
    public static final GameObjectProperty<AABB> BOUNDING_BOX = new GameObjectProperty<>(
            "bounding_box",
            AABB.class,
            BlockGameObject::parseAabb
    );

    // 儲存生成前的原始方塊，用於遊戲結束時還原環境
    private BlockState originalState = null;
    private final List<IBlockBehavior> behaviors = new ArrayList<>();

    protected BlockGameObject(Identifier id, GameObjectKind kind) {
        super(id, kind);
    }

    @Override
    protected void registerProperties() {
        // 預設為空氣，代表不改變世界方塊，僅作座標邏輯使用
        define(BLOCK_STATE, Blocks.AIR.defaultBlockState());
        define(BOUNDING_BOX, null);
    }

    @Override
    public void onInitialize(GameInstance<?, ?, ?> instance) {
        for (IBlockBehavior behavior : behaviors) {
            behavior.onInitialize(this, instance);
        }
    }

    /**
     * 🌟 驗證邏輯：確保座標已設定。
     */
    @Override
    public boolean validate() {
        return get(POS) != null;
    }

    /**
     * 🌟 方塊生成邏輯：
     * 將指定的方塊狀態放置到世界中，並記錄原始方塊以供還原。
     */
    @Override
    protected void onSpawn(GameInstance<?, ?, ?> instance) {
        BlockPos gridPos = BlockPos.containing(getPosition());
        BlockState newState = get(BLOCK_STATE);

        if (!newState.isAir()) {
            // 記錄原本的位置，以便銷毀時還原
            this.originalState = instance.getLevel().getBlockState(gridPos);
            // 放置新方塊
            instance.getLevel().setBlockAndUpdate(gridPos, newState);
        }
    }

    /**
     * 🌟 方塊銷毀邏輯：
     * 如果生成時改變了世界方塊，則將其還原為原本的狀態。
     */
    @Override
    protected void onDestroy(GameInstance<?, ?, ?> instance) {
        for (IBlockBehavior behavior : behaviors) {
            behavior.onDestroy(this, instance);
        }

        if (originalState != null) {
            BlockPos gridPos = BlockPos.containing(getPosition());
            instance.getLevel().setBlockAndUpdate(gridPos, originalState);
            this.originalState = null;
        }
    }

    public void addBehavior(IBlockBehavior behavior) {
        this.behaviors.add(behavior);
    }

    public void removeBehavior(IBlockBehavior behavior) {
        this.behaviors.remove(behavior);
    }

    public BlockPos getBlockPos() {
        return BlockPos.containing(getPosition());
    }

    public boolean containsPos(BlockPos pos) {
        AABB aabb = get(BOUNDING_BOX);
        if (aabb != null) {
            return aabb.contains(pos.getX(), pos.getY(), pos.getZ());
        }
        return pos.equals(getBlockPos());
    }

    private static AABB parseAabb(String input) {
        String[] p = input.split(",");
        if (p.length != 6) {
            throw new IllegalArgumentException("bounding_box must be minX,minY,minZ,maxX,maxY,maxZ");
        }

        double minX = Double.parseDouble(p[0]);
        double minY = Double.parseDouble(p[1]);
        double minZ = Double.parseDouble(p[2]);
        double maxX = Double.parseDouble(p[3]);
        double maxY = Double.parseDouble(p[4]);
        double maxZ = Double.parseDouble(p[5]);

        return new AABB(
                Math.min(minX, maxX),
                Math.min(minY, maxY),
                Math.min(minZ, maxZ),
                Math.max(minX, maxX),
                Math.max(minY, maxY),
                Math.max(minZ, maxZ)
        );
    }
}