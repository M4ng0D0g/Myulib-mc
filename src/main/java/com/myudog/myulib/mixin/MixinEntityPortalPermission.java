package com.myudog.myulib.mixin;

import com.myudog.myulib.api.debug.DebugTraceManager;
import com.myudog.myulib.api.permission.PermissionAction;
import com.myudog.myulib.api.permission.PermissionDecision;
import com.myudog.myulib.api.permission.PermissionGate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class) // 改為針對 Entity 以涵蓋所有實體
public abstract class MixinEntityPortalPermission {

    // 1.21 中進入傳送門的關鍵切入點
    @Inject(method = "setAsInsidePortal", at = @At("HEAD"), cancellable = true, require = 0)
    private void onEnterPortal(CallbackInfo ci) {
        if ((Object) this instanceof ServerPlayer player) {
            DebugTraceManager.begin(player, "setAsInsidePortal");
            DebugTraceManager.step(player, "action=" + PermissionAction.USE_PORTAL);
            PermissionDecision decision = PermissionGate.evaluateDecision(player, PermissionAction.USE_PORTAL, player.position());
            DebugTraceManager.step(player, "decision=" + decision);
            if (decision == PermissionDecision.DENY) {
                ci.cancel();
                DebugTraceManager.end(player, "result=DENY");
                return;
            }
            DebugTraceManager.end(player, "result=ALLOW");
        }
    }
}