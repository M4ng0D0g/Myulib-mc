package com.myudog.myulib.client;

import com.myudog.myulib.client.api.framework.ui.ConfigUiKeybinds;
import com.myudog.myulib.client.api.framework.ui.network.ConfigUiClientNetworking;
import com.myudog.myulib.client.api.field.FieldVisualizationClientRenderer;

public final class MyulibFrameworkApiClient {
    public static void initFrameworkClient() {
        ConfigUiKeybinds.install();
        ConfigUiClientNetworking.install();
        FieldVisualizationClientRenderer.install();
    }
}
