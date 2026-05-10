import os
import shutil

core_dir = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib"
framework_dir = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\main\java\com\myudog\myulib"

core_client_dir = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\client\java\com\myudog\myulib\client"
framework_client_dir = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\client\java\com\myudog\myulib\client"

# -- Core APIs --
core_api = """package com.myudog.myulib.api;

import com.myudog.myulib.api.core.control.ControlManager;
import com.myudog.myulib.api.core.debug.DebugLogManager;
import com.myudog.myulib.api.core.hologram.network.HologramNetworking;
import com.myudog.myulib.api.core.camera.CameraApi;
import com.myudog.myulib.api.core.timer.TimerManager;
import com.myudog.myulib.api.core.ui.network.ConfigUiNetworking;

public final class MyulibApi {
    public static void initCore() {
        AccessSystems.init();
        DebugLogManager.INSTANCE.install();

        CameraApi.initServer();
        ControlManager.INSTANCE.install();
        HologramNetworking.registerPayloads();
        ConfigUiNetworking.registerPayloads();
        ConfigUiNetworking.registerServerReceivers();

        TimerManager.INSTANCE.install();
    }
}
"""

core_api_client = """package com.myudog.myulib.client.api;

import com.myudog.myulib.api.core.camera.CameraApi;
import com.myudog.myulib.api.core.control.ControlManager;
import com.myudog.myulib.api.core.hologram.network.HologramNetworking;
import com.myudog.myulib.api.core.ui.network.ConfigUiNetworking;
import com.myudog.myulib.client.MyuVFXClientManager;

public final class MyulibApiClient {
    public static void initCoreClient() {
        CameraApi.initClient();
        ControlManager.INSTANCE.installClient();
        HologramNetworking.registerClientReceivers();
        ConfigUiNetworking.registerClientReceivers();
        MyuVFXClientManager.INSTANCE.install();
    }
}
"""

# -- Framework APIs --
framework_api = """package com.myudog.myulib.api;

import com.myudog.myulib.api.framework.field.FieldManager;
import com.myudog.myulib.api.framework.field.FieldVisualizationManager;
import com.myudog.myulib.api.framework.game.core.GameManager;
import com.myudog.myulib.api.framework.rolegroup.RoleGroupManager;
import com.myudog.myulib.api.framework.permission.PermissionManager;
import com.myudog.myulib.api.framework.team.TeamManager;

public final class MyulibApi {
    public static void initFramework() {
        FieldManager.INSTANCE.install();
        FieldVisualizationManager.INSTANCE.install();
        GameManager.INSTANCE.install();
        PermissionManager.INSTANCE.install();
        RoleGroupManager.INSTANCE.install();
        TeamManager.INSTANCE.install();
    }
}
"""

framework_api_client = """package com.myudog.myulib.client.api;

import com.myudog.myulib.api.framework.field.FieldVisualizationManager;

public final class MyulibApiClient {
    public static void initFrameworkClient() {
        FieldVisualizationManager.INSTANCE.installClient();
    }
}
"""

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)

write_file(os.path.join(core_dir, "api", "MyulibApi.java"), core_api)
write_file(os.path.join(core_client_dir, "api", "MyulibApiClient.java"), core_api_client)

write_file(os.path.join(framework_dir, "api", "MyulibApi.java"), framework_api)
write_file(os.path.join(framework_client_dir, "api", "MyulibApiClient.java"), framework_api_client)

print("APIs updated.")
