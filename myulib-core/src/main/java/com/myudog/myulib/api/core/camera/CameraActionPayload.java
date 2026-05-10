package com.myudog.myulib.api.core.camera;

import com.myudog.myulib.api.core.animation.Easing;
import net.minecraft.world.phys.Vec3;

public record CameraActionPayload(
        ActionType action,
        float intensity,
        long durationMillis,

        // --- 擴充：位置資訊 ---
        Vec3 targetStaticPos,     // 靜態目標座標 (可能為 null)
        Integer targetEntityId,   // 動態目標實體 ID (可能為 null)

        // --- 擴充：視角資訊 ---
        Vec3 lookAtStaticPos,     // 靜態注視點 (可能為 null)
        Integer lookAtEntityId,   // 動態注視實體 ID (可能為 null)

        Vec3 offset,              // 攝影機偏移量
        Easing easing
) {
    public enum ActionType {
        SHAKE,
        MOVE_TO,
        RESET
    }

    public static CameraActionPayload shake(float intensity, long durationMillis) {
        return new CameraActionPayload(
                ActionType.SHAKE, intensity, durationMillis,
                null, null, null, null, Vec3.ZERO, Easing.SMOOTH_STEP
        );
    }

    // 🌟 接收全新的 CameraTrackingTarget 並解構打包
    public static CameraActionPayload moveTo(CameraTrackingTarget target, long durationMillis, Easing easing) {
        return new CameraActionPayload(
                ActionType.MOVE_TO, 0.0f, durationMillis,
                target.getStaticPosition(),
                target.getEntityId(),
                target.getStaticLookAt(),
                target.getLookAtEntityId(),
                target.getOffset(),
                easing == null ? Easing.SMOOTH_STEP : easing
        );
    }

    public static CameraActionPayload reset() {
        return new CameraActionPayload(
                ActionType.RESET, 0.0f, 0L,
                null, null, null, null, Vec3.ZERO, Easing.LINEAR
        );
    }

    // 💡 小提醒：當你要將這個 Record 註冊進 Fabric 的網路封包系統 (ServerPlayNetworking) 時，
    // 記得針對這些 nullable 的欄位進行 readBoolean() / writeBoolean() 的判斷處理喔！
}