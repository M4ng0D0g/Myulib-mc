package com.myudog.myulib.api.framework.game.features;

import com.myudog.myulib.api.framework.field.FieldDefinition;
import com.myudog.myulib.api.framework.field.FieldManager;
import com.myudog.myulib.api.framework.game.GameInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FieldFeatureImpl implements FieldFeature {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldFeatureImpl.class.getName());

    // 🌟 本地追蹤器：只記錄屬於「這個房間」的 Field ID
    private final Set<java.util.UUID> activeFields = ConcurrentHashMap.newKeySet();

    /**
     * 將一個已經存在於全域 FieldManager 的區域，綁定到這個房間。
     * @return 綁定是否成功
     */
    @Override
    public boolean bindField(@NotNull java.util.UUID fieldId) {
        if (FieldManager.INSTANCE.get(fieldId) == null) {
            LOGGER.warn("嘗試綁定不存在的區域: {}", fieldId);
            return false;
        }
        activeFields.add(fieldId);
        return true;
    }

    /**
     * 檢查給定座標是否落在本房間的「任何一個」綁定區域內。
     * 應用場景：檢查玩家是否在遊戲規定的邊界範圍內。
     */
    @Override
    public boolean isInsideGameBounds(@NotNull Identifier dimensionId, @NotNull Vec3 position) {
        // 利用 FieldManager 的全域查詢
        Optional<FieldDefinition> found = FieldManager.INSTANCE.findAt(dimensionId, position);

        // 確保找到的區域，是屬於這個房間的
        return found.isPresent() && activeFields.contains(found.get().uuid());
    }

    /**
     * 獲取給定座標上，屬於本房間的所有區域 ID (應付可能的未來重疊設計)。
     */
    @Override
    public Set<java.util.UUID> getFieldsAt(@NotNull Identifier dimensionId, @NotNull Vec3 position) {
        return activeFields.stream()
                .map(FieldManager.INSTANCE::get)
                .filter(def -> def != null && def.dimensionId().equals(dimensionId) && def.bounds().contains(position))
                .map(FieldDefinition::uuid)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * 取得目前房間綁定的所有區域快照
     */
    @Override
    public Set<java.util.UUID> getActiveFields() {
        return Set.copyOf(activeFields);
    }

    @Override
    public void unbindField(@NotNull java.util.UUID fieldId) {
        activeFields.remove(fieldId);
    }

    @Override
    public void clean(GameInstance<?, ?, ?> instance) {
        // 🌟 核心清理邏輯：當房間結束時，應該做兩件事：
        // 1. 將屬於這個房間的區域從全域 FieldManager 中註銷
        for (java.util.UUID fieldId : activeFields) {
            FieldManager.INSTANCE.unregister(fieldId);
        }

        // 2. 清空本地追蹤器
        activeFields.clear();
    }
}