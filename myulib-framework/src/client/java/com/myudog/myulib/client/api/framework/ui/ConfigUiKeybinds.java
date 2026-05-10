package com.myudog.myulib.client.api.framework.ui;

import com.mojang.blaze3d.platform.InputConstants;
import com.myudog.myulib.client.api.framework.ui.screen.ConfigRootScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import org.lwjgl.glfw.GLFW;

public final class ConfigUiKeybinds {
    private static KeyMapping OPEN_CONFIG_KEY;
    private static boolean installed;

    private ConfigUiKeybinds() {
    }

    public static void install() {
        if (installed) {
            return;
        }
        installed = true;

        OPEN_CONFIG_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.myulib.open_config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_CONFIG_KEY.consumeClick()) {
                openConfig(client);
            }
        });
    }

    private static void openConfig(Minecraft client) {
        if (client.player == null) {
            return;
        }

        boolean isOp = client.player.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS));
        if (!isOp) {
            client.player.sendOverlayMessage(Component.literal("你需要 OP 才能開啟設定"));
            return;
        }

        boolean readonly = !client.player.getAbilities().instabuild;
        if (readonly) {
            client.player.sendOverlayMessage(Component.literal("目前為唯讀模式：OP 需在創造模式才能修改"));
        }

        ConfigRootScreen.openForCurrentPlayer(readonly);
    }
}



