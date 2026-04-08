package com.myudog.myulib.api;

import com.myudog.myulib.api.command.AccessCommandService;
import com.myudog.myulib.api.command.CommandRegistry;
import com.myudog.myulib.api.ui.ConfigurationUiRegistry;
import com.myudog.myulib.api.ui.NoopConfigurationUiBridge;

public final class AccessSystems {
    private AccessSystems() {
    }

    public static void init() {
        ConfigurationUiRegistry.setBridge(NoopConfigurationUiBridge.INSTANCE);
        CommandRegistry.clear();
        AccessCommandService.registerDefaults();
    }
}

