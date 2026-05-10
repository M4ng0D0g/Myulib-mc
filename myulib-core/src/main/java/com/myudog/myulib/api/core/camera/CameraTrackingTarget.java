package com.myudog.myulib.api.core.camera;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * 代表攝影機追蹤的目標。
 * 可以是靜態座標，也可以是動態實體 (儲存實體 ID 以支援網路傳輸與即時追蹤)。
 */
public final class CameraTrackingTarget {

    // --- 核心資料 (可安全序列化傳送) ---
    @Nullable private final Vec3 staticPosition;
    @Nullable private final Integer entityId;

    @Nullable private final Vec3 staticLookAt;
    @Nullable private final Integer lookAtEntityId;

    private final Vec3 offset;

    // 私有全參數建構子
    private CameraTrackingTarget(
            @Nullable Vec3 staticPosition,
            @Nullable Integer entityId,
            @Nullable Vec3 staticLookAt,
            @Nullable Integer lookAtEntityId,
            Vec3 offset) {
        this.staticPosition = staticPosition;
        this.entityId = entityId;
        this.staticLookAt = staticLookAt;
        this.lookAtEntityId = lookAtEntityId;
        this.offset = offset == null ? Vec3.ZERO : offset;
    }

    // --- 靜態工廠方法 (建立起點) ---

    public static CameraTrackingTarget of(Entity entity) {
        Objects.requireNonNull(entity, "entity");
        // 記錄實體 ID，而非當下的靜態座標
        return new CameraTrackingTarget(null, entity.getId(), null, null, Vec3.ZERO);
    }

    public static CameraTrackingTarget of(Vec3 position) {
        Objects.requireNonNull(position, "position");
        return new CameraTrackingTarget(position, null, null, null, Vec3.ZERO);
    }

    // --- 鏈式設定 (Builder 模式擴充目標屬性) ---

    public CameraTrackingTarget withOffset(Vec3 offset) {
        return new CameraTrackingTarget(this.staticPosition, this.entityId, this.staticLookAt, this.lookAtEntityId, offset);
    }

    public CameraTrackingTarget lookAt(Entity entity) {
        Objects.requireNonNull(entity, "entity");
        return new CameraTrackingTarget(this.staticPosition, this.entityId, null, entity.getId(), this.offset);
    }

    public CameraTrackingTarget lookAt(Vec3 point) {
        Objects.requireNonNull(point, "point");
        return new CameraTrackingTarget(this.staticPosition, this.entityId, point, null, this.offset);
    }

    // --- 資料讀取 (供 API 打包成 Payload 封包時使用) ---

    @Nullable public Vec3 getStaticPosition() { return staticPosition; }
    @Nullable public Integer getEntityId() { return entityId; }
    @Nullable public Vec3 getStaticLookAt() { return staticLookAt; }
    @Nullable public Integer getLookAtEntityId() { return lookAtEntityId; }
    public Vec3 getOffset() { return offset; }

    // --- 客戶端即時解析邏輯 (Client-Side Rendering) ---

    /**
     * 根據當前世界 (Level) 即時解析出當下的絕對座標。
     * ⚠️ 客戶端在每一幀 (Tick/Frame) 運算攝影機位置時，都應該呼叫這個方法，這樣實體移動時攝影機才會跟著動！
     *
     * @param level 客戶端的 Level (Minecraft.getInstance().level)
     */
    public Vec3 resolvePosition(Level level) {
        Vec3 basePos = null;

        if (staticPosition != null) {
            basePos = staticPosition;
        } else if (entityId != null && level != null) {
            // 即時尋找該實體
            Entity entity = level.getEntity(entityId);
            if (entity != null) {
                basePos = entity.position();
            }
        }

        // 防呆機制：如果找不到實體 (例如實體被刪除了或跑到渲染範圍外)，回傳原點加上 offset 避免崩潰
        if (basePos == null) {
            return offset;
        }
        return basePos.add(offset);
    }

    /**
     * 根據當前世界 (Level) 即時解析出當下的注視點 (LookAt) 座標。
     */
    @Nullable
    public Vec3 resolveLookAt(Level level) {
        if (staticLookAt != null) {
            return staticLookAt;
        } else if (lookAtEntityId != null && level != null) {
            Entity entity = level.getEntity(lookAtEntityId);
            if (entity != null) {
                // 💡 優化細節：追蹤實體視角時，通常看著牠身體的一半高度 (重心) 會比看著腳底板 (position) 更自然
                return entity.position().add(0, entity.getBbHeight() / 2.0, 0);
            }
        }
        return null; // 如果沒有設定 LookAt，回傳 null 代表由攝影機系統維持原本的 Yaw/Pitch
    }
}