import os

core_dir = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib"
framework_dir = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\main\java\com\myudog\myulib"

core_client_dir = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\client\java\com\myudog\myulib\client"
framework_client_dir = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\client\java\com\myudog\myulib\client"

# --- CORE ---
# MyulibCore.java
core_main_content = """package com.myudog.myulib;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MyulibCore implements ModInitializer {
    public static final String MOD_ID = "myulib-core";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path.trim().toLowerCase());
    }

    @Override
    public void onInitialize() {
        LOGGER.info("MyuLib Core is initializing...");
        com.myudog.myulib.api.MyulibApi.initCore();
        LOGGER.info("MyuLib Core initialized.");
    }
}
"""

# MyulibCoreClient.java
core_client_content = """package com.myudog.myulib.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MyulibCoreClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("myulib-core-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("MyuLib Core Client is initializing...");
        com.myudog.myulib.api.MyulibApi.initCoreClient();
        LOGGER.info("MyuLib Core Client initialized.");
    }
}
"""

# --- FRAMEWORK ---
# MyulibFramework.java
framework_main_content = """package com.myudog.myulib;

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
"""

# MyulibFrameworkClient.java
framework_client_content = """package com.myudog.myulib.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MyulibFrameworkClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("myulib-framework-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("MyuLib Framework Client is initializing...");
        com.myudog.myulib.api.MyulibApi.initFrameworkClient();
        LOGGER.info("MyuLib Framework Client initialized.");
    }
}
"""

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)

write_file(os.path.join(core_dir, "MyulibCore.java"), core_main_content)
write_file(os.path.join(core_client_dir, "MyulibCoreClient.java"), core_client_content)

write_file(os.path.join(framework_dir, "MyulibFramework.java"), framework_main_content)
write_file(os.path.join(framework_client_dir, "MyulibFrameworkClient.java"), framework_client_content)

# Delete old entry points
for old_file in [os.path.join(core_dir, "Myulib.java"), os.path.join(core_client_dir, "MyulibClient.java")]:
    if os.path.exists(old_file):
        os.remove(old_file)

print("Entrypoints updated.")
