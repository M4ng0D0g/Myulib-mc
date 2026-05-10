package com.myudog.myulib.api.framework.game.features;

import com.myudog.myulib.api.framework.game.core.GameInstance;
import com.myudog.myulib.api.core.object.IObjectDef;
import com.myudog.myulib.api.core.object.IObjectRt;
import com.myudog.myulib.api.core.object.ObjectManager;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectFeatureImpl implements ObjectFeature {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectFeatureImpl.class.getName());

    // 🌟 必須使用 ConcurrentHashMap，確保多執行緒下遊戲事件與 Tick 更新的安全性
    private final Map<Identifier, IObjectRt> runtimeObjects = new ConcurrentHashMap<>();

    @Override
    public void addRuntimeObject(@NotNull Identifier instanceId, @NotNull IObjectRt obj) {
        this.runtimeObjects.put(instanceId, obj);
    }

    @Override
    public Optional<IObjectRt> getObject(@NotNull Identifier instanceId) {
        return Optional.ofNullable(runtimeObjects.get(instanceId));
    }

    @Override
    public Collection<IObjectRt> getRuntimeObjects() {
        return Collections.unmodifiableCollection(runtimeObjects.values());
    }

    /**
     * 🌟 核心生成邏輯：透過藍圖生成實體
     * @param instance 當前的遊戲實例 (提供世界與上下文)
     * @param defId    藍圖的 ID (對應 ObjectManager 中的 ObjectDef)
     * @param instanceId 這個 Runtime 物件的唯一識別碼 (例如 "zombie_spawner_1")
     * @return 生成的 Runtime 物件
     */
    @Override
    public IObjectRt spawnObject(GameInstance<?, ?, ?> instance, Identifier defId, Identifier instanceId) {
        // 1. 從全域管理器獲取藍圖
        IObjectDef def = ObjectManager.INSTANCE.getDefinition(defId);
        if (def == null) {
            throw new IllegalArgumentException("無法生成物件，找不到對應的 ObjectDef: " + defId);
        }

        // 2. 呼叫藍圖的 spawn 方法，傳入 instance 以獲取 ServerLevel 等資訊
        IObjectRt rtObj = def.spawn(instance);

        // 3. 初始化並生成實例，確保 behavior / 事件綁定生效
        rtObj.onInitialize(instance);
        rtObj.spawn(instance);

        // 4. 儲存至本地追蹤器
        this.runtimeObjects.put(instanceId, rtObj);

        return rtObj;
    }

    /**
     * 🌟 核心清理邏輯：對應 GameData 卸載時的回收
     * 負責呼叫所有 Runtime 物件的 destroy，以移除 Minecraft 實體或還原方塊。
     */
    @Override
    public void clean(GameInstance<?, ?, ?> instance) {
        for (Map.Entry<Identifier, IObjectRt> entry : runtimeObjects.entrySet()) {
            try {
                // 呼叫下游實作的銷毀邏輯 (例如 entity.discard() 或 Block 還原)
                entry.getValue().destroy(instance);
            } catch (Exception e) {
                // ⚠️ 加上 try-catch 防護：避免單一物件銷毀失敗，導致其他物件殘留在世界上
                LOGGER.error("銷毀 Runtime 物件時發生錯誤: {}", entry.getKey(), e);
            }
        }
        // 徹底清空記憶體參照
        this.runtimeObjects.clear();
    }
}