package com.myudog.myulib.api;

import com.myudog.myulib.api.framework.command.AccessCommandService;
import com.myudog.myulib.api.core.command.CommandRegistry;
import com.myudog.myulib.api.framework.ui.ConfigurationUiRegistry;
import com.myudog.myulib.api.framework.ui.NoopConfigurationUiBridge;

public final class AccessSystems {
    private AccessSystems() {
    }

    public static void init() {
        ConfigurationUiRegistry.setBridge(NoopConfigurationUiBridge.INSTANCE);
        CommandRegistry.clear();
        AccessCommandService.registerDefaults();
    }
}

