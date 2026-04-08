package com.myudog.myulib.mixin;

import com.myudog.myulib.api.game.GameManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	// 【關鍵修正】將目標方法改為 Mojang 官方名稱 "tickServer"
	@Inject(method = "tickServer", at = @At("TAIL"))
	private void myulib$tick(BooleanSupplier hasTimeLeft, CallbackInfo info) {
		GameManager.tickAll();
	}
}