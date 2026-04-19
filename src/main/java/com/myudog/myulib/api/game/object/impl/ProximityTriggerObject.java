package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.core.Property;
import com.myudog.myulib.api.game.object.IGameObject;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 範圍觸發器物件。
 * 支援：靜態座標/動態生物綁定、球體(SPHERE)/AABB 判定。
 */
public class ProximityTriggerObject extends BaseGameObject {

    // 🌟 定義強型別屬性
    public static final Property<Double> RADIUS = new Property<>(
            "radius", Double.class, Double::parseDouble
    );
    public static final Property<String> SHAPE = new Property<>(
            "shape", String.class, String::toUpperCase
    );
    public static final Property<UUID> TARGET = new Property<>(
            "target", UUID.class, UUID::fromString
    );

    // 🌟 新增 AABB 專用的維度屬性 (解析如 "2.0,2.0,2.0" 的字串)
    public static final Property<Vec3> DIMENSIONS = new Property<>(
            "dimensions", Vec3.class, s -> {
        String[] parts = s.split(",");
        if (parts.length >= 3) {
            return new Vec3(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
        }
        return new Vec3(2, 2, 2);
    }
    );

    // 🌟 狀態變數：防止重複觸發、快取追蹤實體
    private final Set<UUID> playersInZone = new HashSet<>();
    private Entity trackedEntity = null;

    public ProximityTriggerObject(Identifier id) {
        super(id, GameObjectKind.PROXIMITY_TRIGGER);
    }

    @Override
    protected void registerProperties() {
        define(RADIUS, 5.0);
        define(SHAPE, "SPHERE");
        define(TARGET, null); // 若為 null 則代表不綁定實體，使用固定座標 POS
        define(DIMENSIONS, new Vec3(2, 2, 2));
    }

    /**
     * 驗證邏輯：必須要有靜態座標 (POS) 或是動態目標 (TARGET)。
     */
    @Override
    public boolean validate() {
        return get(POS) != null || get(TARGET) != null;
    }

    @Override
    public void onInitialize(GameInstance<?, ?, ?> instance) {
        // 取得該物件所在的正確維度
        ServerLevel targetWorld = instance.getLevel();

        // 自動綁定範圍偵測 (暫時註解，等待 PlayerMoveEvent 實作)
        /*
        instance.getEventBus().subscribe(PlayerMoveEvent.class, event -> {
            ServerPlayer player = event.getPlayer();
            UUID playerId = player.getUUID();

            // 確保玩家與物件處於同一維度，再進行距離判定
            boolean inRange = (player.serverLevel() == targetWorld) && shouldTrigger(instance, player);

            if (inRange) {
                playersInZone.add(playerId);
                // instance.getEventBus().dispatch(new PlayerEnterAreaEvent(this, player));
            } else {
                playersInZone.remove(playerId);
                // instance.getEventBus().dispatch(new PlayerLeaveAreaEvent(this, player));
            }
        });
        */
    }

    /**
     * 計算中心點：若有綁定 TARGET，則即時回傳實體座標；否則回傳固定 POS。
     */
    private Vec3 getCenter(GameInstance<?, ?, ?> instance) {
        UUID targetUuid = get(TARGET);

        if (targetUuid != null) {
            // 如果快取的實體不存在或已死亡，嘗試重新從世界中獲取
            if (trackedEntity == null || !trackedEntity.isAlive()) {
                trackedEntity = instance.getLevel().getEntity(targetUuid);
            }
            return trackedEntity != null ? trackedEntity.position() : null;
        }

        // 沒有綁定實體，回傳 BaseGameObject 的固定座標
        return get(POS);
    }

    /**
     * 判定邏輯：依據形狀屬性 (SPHERE 或 AABB) 檢查玩家是否在範圍內。
     */
    private boolean shouldTrigger(GameInstance<?, ?, ?> instance, ServerPlayer player) {
        Vec3 center = getCenter(instance);
        if (center == null) return false;

        String shape = get(SHAPE);
        Vec3 playerPos = player.position();

        if ("AABB".equals(shape)) {
            Vec3 dim = get(DIMENSIONS);
            AABB box = new AABB(center.subtract(dim), center.add(dim));
            return box.contains(playerPos);
        } else {
            // 預設為球體 (SPHERE)
            double radius = get(RADIUS);
            return playerPos.distanceToSqr(center) <= (radius * radius);
        }
    }

    /**
     * 虛擬範圍物件不需要物理實體，覆寫 onSpawn 為空。
     */
    @Override
    protected void onSpawn(GameInstance<?, ?, ?> instance) {
        // [EMPTY]
    }

    /**
     * 遊戲結束或物件銷毀時，清理記憶體防止 Memory Leak。
     */
    @Override
    protected void onDestroy(GameInstance<?, ?, ?> instance) {
        playersInZone.clear();
        trackedEntity = null;
    }

    @Override
    public IGameObject copy() {
        ProximityTriggerObject clone = new ProximityTriggerObject(this.id);
        copyBaseStateTo(clone);
        return clone;
    }
}