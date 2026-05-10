package com.myudog.myulib;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MyulibFramework implements ModInitializer {
    public static final String MOD_ID = "myulib-framework";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path.trim().toLowerCase());
    }

    @Override
    public void onInitialize() {
        LOGGER.info("MyuLib Framework is initializing...");
        com.myudog.myulib.api.MyulibApi.initFramework();
        LOGGER.info("MyuLib Framework initialized.");
    }
}
