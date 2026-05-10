package com.myudog.myulib.api.core.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

/**
 * 空間滯留效果的擴充配置。
 * 封裝了原生效果，同時允許擴充自訂 UI 圖標、是否允許被牛奶解除等進階屬性。
 */
public record SpatialEffect(
        Identifier id,
        MobEffect vanillaEffect,
        int amplifier,
        boolean showCustomIcon // 標示是否需要 Client 進行特殊 UI 渲染
        // 未來可擴充: boolean isCurableByMilk, Identifier customIconTexture
) {}