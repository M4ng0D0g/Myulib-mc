package com.myudog.myulib.api;

import com.myudog.myulib.api.core.command.AccessCommandService;
import com.myudog.myulib.api.core.command.CommandRegistry;
import com.myudog.myulib.api.core.ui.ConfigurationUiRegistry;
import com.myudog.myulib.api.core.ui.NoopConfigurationUiBridge;

public final class AccessSystems {
    private AccessSystems() {
    }

    public static void init() {
        ConfigurationUiRegistry.setBridge(NoopConfigurationUiBridge.INSTANCE);
        CommandRegistry.clear();
        AccessCommandService.registerDefaults();
    }
}

