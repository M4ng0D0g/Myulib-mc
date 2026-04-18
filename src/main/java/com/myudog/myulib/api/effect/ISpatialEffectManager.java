package com.myudog.myulib.api.effect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import java.util.List;
import java.util.UUID;

/**
 * 空間效果管理器標準介面。
 */
public interface ISpatialEffectManager {
    void addEffectSource(ServerPlayer player, SpatialEffect effect);

    void removeEffectSource(ServerPlayer player, SpatialEffect effect);

    void clearPlayer(ServerPlayer player);

    void clearPlayer(UUID playerId);

    boolean hasAnySpatialEffect(UUID playerId);

    boolean isSpatialEffect(UUID playerId, Holder<MobEffect> effectHolder);

    /**
     * 🌟 提供給 Client 渲染或外部系統獲取狀態的接口。
     * Client 端收到網路封包 (Packet) 同步後，即可呼叫此方法取得要畫在 HUD 上的效果。
     */
    List<SpatialEffect> getActiveEffects(UUID playerId);
}