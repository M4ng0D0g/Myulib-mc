package com.myudog.myulib.api.core.object;

import com.myudog.myulib.api.core.event.EventBus;
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
 * 完全與 GameInstance 解耦，任何人都能使用。
 */
public final class ObjectManager {

    public static final ObjectManager INSTANCE = new ObjectManager();

    private final Map<Identifier, IObjectDef> definitions = new ConcurrentHashMap<>();
    
    // 原生事件全域廣播中心 (讓 Framework 的 GameInstance 或其他系統可以掛載監聽)
    private final EventBus nativeEventBus = new EventBus();
    
    // 追蹤實體/方塊對應的 IObjectRt (用於直接轉發事件給特定物件，如果需要的話)
    private final Map<UUID, IObjectRt> entityObjects = new ConcurrentHashMap<>();
    private final Map<BlockPos, IObjectRt> blockObjects = new ConcurrentHashMap<>();

    private ObjectManager() {}

    public EventBus getNativeEventBus() {
        return nativeEventBus;
    }

    public void registerDefinition(Identifier id, IObjectDef def) {
        definitions.put(id, def);
    }

    public IObjectDef getDefinition(Identifier defId) {
        return definitions.get(defId);
    }

    // --- 物件註冊 API ---
    public void registerEntityObject(UUID entityUuid, IObjectRt obj) {
        entityObjects.put(entityUuid, obj);
    }
    public void unregisterEntityObject(UUID entityUuid) {
        entityObjects.remove(entityUuid);
    }
    public void registerBlockObject(BlockPos pos, IObjectRt obj) {
        blockObjects.put(pos, obj);
    }
    public void unregisterBlockObject(BlockPos pos) {
        blockObjects.remove(pos);
    }

    // --- 事件攔截與分發 (供 Mixin 呼叫) ---

    public boolean handleEntityDamage(LivingEntity victim, DamageSource source, float amount) {
        EntityDamageEvent event = new EntityDamageEvent(victim, source, amount);
        nativeEventBus.dispatch(event);
        return event.isCanceled();
    }

    public void handleEntityDeath(LivingEntity victim, DamageSource source) {
        EntityDeathEvent event = new EntityDeathEvent(victim, source);
        nativeEventBus.dispatch(event);
        entityObjects.remove(victim.getUUID());
    }

    public boolean handleEntityInteract(ServerPlayer player, Entity target, InteractionHand hand) {
        EntityInteractEvent event = new EntityInteractEvent(player, target, hand);
        nativeEventBus.dispatch(event);
        return event.isCanceled();
    }

    public boolean handleBlockBreak(ServerPlayer player, BlockPos pos, ServerLevel level) {
        BlockBreakEvent event = new BlockBreakEvent(player, pos, level);
        nativeEventBus.dispatch(event);
        return event.isCanceled();
    }

    public boolean handleBlockInteract(ServerPlayer player, BlockPos pos, ServerLevel level) {
        BlockInteractEvent event = new BlockInteractEvent(player, pos, level);
        nativeEventBus.dispatch(event);
        return event.isCanceled();
    }
}