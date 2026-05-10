package com.myudog.myulib.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MyulibFrameworkClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("myulib-framework-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("MyuLib Framework Client is initializing...");
        MyulibFrameworkApiClient.initFrameworkClient();
        LOGGER.info("MyuLib Framework Client initialized.");
    }
}
