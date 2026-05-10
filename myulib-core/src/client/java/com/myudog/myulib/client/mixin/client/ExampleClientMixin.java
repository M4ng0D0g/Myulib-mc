package com.myudog.myulib.client.mixin.client;

import net.minecraft.client.Minecraft; // 【關鍵修正】引入官方名稱的 Minecraft 類別;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 【關鍵修正】將目標指定為 Minecraft.class
@Mixin(Minecraft.class)
public class ExampleClientMixin {

	@Inject(at = @At("HEAD"), method = "run")
	private void init(CallbackInfo info) {
		// 這段程式碼會在遊戲客戶端啟動並進入主迴圈 (Minecraft.run) 時執行
		System.out.println("====== Myulib 客戶端 Mixin 已成功載入！======");
	}
}