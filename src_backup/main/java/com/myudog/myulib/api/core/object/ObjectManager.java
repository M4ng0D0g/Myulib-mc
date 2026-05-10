package com.myudog.myulib.api.core.object;

import com.myudog.myulib.api.framework.game.core.GameInstance;
import com.myudog.myulib.api.framework.game.core.GameManager;
import com.myudog.myulib.api.core.object.event.BlockBreakEvent;
import com.myudog.myulib.api.core.object.event.BlockInteractEvent;
import com.myudog.myulib.api.core.object.event.EntityDamageEvent;
import com.myudog.myulib.api.core.object.event.EntityDeathEvent;
import com.myudog.myulib.api.core.object.event.EntityInteractEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全域遊戲物件與事件攔截中心。
 * 負責追蹤 Minecraft 實體與 myulib 物件的對應關係，並分發 Mixin 攔截到的原生事件。
 */
public final class ObjectManager {

    public static final ObjectManager INSTANCE = new ObjectManager();

    // 追蹤實體 UUID -> GameInstance
    private final Map<UUID, GameInstance<?, ?, ?>> entityToInstanceMap = new ConcurrentHashMap<>();
    private final Map<Identifier, IObjectDef> definitions = new ConcurrentHashMap<>();

    private ObjectManager() {}

    // --- 實體註冊 API (供 EntityGameObject 呼叫) ---

    public void registerEntity(UUID entityUuid, GameInstance<?, ?, ?> instance) {
        if (instance == null) throw new IllegalArgumentException("instance 不能為 null");
        GameInstance<?, ?, ?> existing = entityToInstanceMap.putIfAbsent(entityUuid, instance);
        if (existing != null && existing != instance) {
            throw new IllegalStateException("實體已註冊於不同的 instance: " + entityUuid);
        }
    }

    public void unregisterEntity(UUID entityUuid) {
        entityToInstanceMap.remove(entityUuid);
    }

    public IObjectDef getDefinition(Identifier defId) {
        return definitions.get(defId);
    }

    // --- 事件攔截與分發 (供 Mixin 呼叫) ---

    public boolean handleEntityDamage(LivingEntity victim, DamageSource source, float amount) {
        GameInstance<?, ?, ?> instance = entityToInstanceMap.get(victim.getUUID());
        if (instance == null || !instance.isEnabled() || !instance.isStarted()) return false;

        EntityDamageEvent event = new EntityDamageEvent(victim, source, amount);
        instance.getEventBus().dispatch(event);
        return event.isCanceled();
    }

    public void handleEntityDeath(LivingEntity victim, DamageSource source) {
        GameInstance<?, ?, ?> instance = entityToInstanceMap.get(victim.getUUID());
        if (instance != null && instance.isEnabled() && instance.isStarted()) {
            instance.getEventBus().dispatch(new EntityDeathEvent(victim, source));
        }
        // 實體死亡後自動解除註冊
        entityToInstanceMap.remove(victim.getUUID());
    }

    public boolean handleEntityInteract(ServerPlayer player, Entity target, InteractionHand hand) {
        GameInstance<?, ?, ?> instance = entityToInstanceMap.get(target.getUUID());
        if (instance == null || !instance.isEnabled() || !instance.isStarted()) return false;

        EntityInteractEvent event = new EntityInteractEvent(player, target, hand);
        instance.getEventBus().dispatch(event);
        return event.isCanceled();
    }

    public boolean handleBlockBreak(ServerPlayer player, BlockPos pos, ServerLevel level) {
        GameInstance<?, ?, ?> instance = resolveInstanceForBlock(player, pos, level);
        if (instance == null || !instance.isEnabled() || !instance.isStarted()) return false;

        BlockBreakEvent event = new BlockBreakEvent(player, pos, level);
        instance.getEventBus().dispatch(event);
        return event.isCanceled();
    }

    public boolean handleBlockInteract(ServerPlayer player, BlockPos pos, ServerLevel level) {
        GameInstance<?, ?, ?> instance = resolveInstanceForBlock(player, pos, level);
        if (instance == null || !instance.isEnabled() || !instance.isStarted()) return false;

        BlockInteractEvent event = new BlockInteractEvent(player, pos, level);
        instance.getEventBus().dispatch(event);
        return event.isCanceled();
    }

    // --- 內部輔助方法 ---

    private GameInstance<?, ?, ?> resolveInstanceForBlock(ServerPlayer player, BlockPos pos, ServerLevel level) {
        // 這邊目前依然保留遍歷的設計，未來如果方塊數量龐大，可以考慮在 ObjectManager 建立 BlockPos -> Instance 的 Spatial HashMap
        // 但因為我們將這個邏輯抽離了 GameManager，未來的優化會更容易且不影響其他系統。

        // 暫時透過 GameManager 取得所有 Instance 進行比對 (或者可以要求 Instance 註冊自己到 ObjectManager)
        // 為了降低與 GameManager 的耦合，最好的做法是維護一個 Map<Integer, GameInstance> 或者直接讓方塊也註冊自己的位置。

        // 簡化實作：遍歷所有活躍的遊戲實例 (需要確保不循環依賴)
        for (GameInstance<?, ?, ?> candidate : GameManager.INSTANCE.getInstances()) {
            if (!candidate.isEnabled() || !candidate.isStarted() || candidate.getLevel() != level) continue;

            for (IObjectRt runtimeObject : candidate.getData().OBJECT_FEATURE.getRuntimeObjects()) {
                var objectPos = runtimeObject.getPosition();
                if (objectPos != null
                        && (int) objectPos.x == pos.getX()
                        && (int) objectPos.y == pos.getY()
                        && (int) objectPos.z == pos.getZ()) {
                    return candidate;
                }
            }
        }
        return null;
    }
}