package com.myudog.myulib.api.framework.game.features;

import com.myudog.myulib.api.framework.game.GameInstance;
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

    // 🌟 強烈建議使用 ConcurrentHashMap，確保並發存取與非同步 Tick 更新時的執行緒安全
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
     * 🌟 實作物件生成邏輯
     * @param instance   當前的遊戲實例 (提供上下文)
     * @param defId      藍圖 ID (對應 ObjectManager 中的 ObjectDef)
     * @param instanceId 這個 Runtime 物件的唯一識別碼 (例如 "zombie_spawner_1")
     * @return 生成的 Runtime 實例
     */
    @Override
    public IObjectRt spawnObject(GameInstance<?, ?, ?> instance, Identifier defId, Identifier instanceId) {
        // 1. 從全域管理器取得藍圖
        IObjectDef def = ObjectManager.INSTANCE.getDefinition(defId);
        if (def == null) {
            throw new IllegalArgumentException("無法生成物件，找不到對應的 ObjectDef: " + defId);
        }

        // 2. 呼叫藍圖的生成方法建立實體
        IObjectRt rtObj = def.spawn();

        // 3. 初始化並在世界上生成實體
        rtObj.onInitialize();
        rtObj.spawn();

        // 4. 將生成的實例存入本地追蹤清單
        this.runtimeObjects.put(instanceId, rtObj);

        return rtObj;
    }

    /**
     * 🌟 實作清理邏輯：在 GameData 銷毀時觸發
     * 確保呼叫所有 Runtime 物件的 destroy，以移除 Minecraft 實體或釋放資源。
     */
    @Override
    public void clean(GameInstance<?, ?, ?> instance) {
        for (Map.Entry<Identifier, IObjectRt> entry : runtimeObjects.entrySet()) {
            try {
                entry.getValue().destroy();
            } catch (Exception e) {
                LOGGER.error("銷毀 Runtime 物件時發生錯誤: {}", entry.getKey(), e);
            }
        }
        this.runtimeObjects.clear();
    }
}