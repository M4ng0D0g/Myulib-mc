import os
import shutil
import re

core_mixin = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib\mixin"
framework_mixin = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\main\java\com\myudog\myulib\mixin"

os.makedirs(framework_mixin, exist_ok=True)

# Move remaining permission-coupled mixins
remaining = ["MixinPlayerInteractionManager.java", "MixinPlayerInteractEntity.java", "MixinServerPlayerGameMode.java"]
for f in remaining:
    src = os.path.join(core_mixin, f)
    dst = os.path.join(framework_mixin, f)
    if os.path.exists(src):
        shutil.move(src, dst)
        print(f"Moved {f} -> framework mixin")

# Fix StateChangeEvent - remove GameInstance dependency completely
sce_path = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib\api\core\object\event\StateChangeEvent.java"
new_sce = '''package com.myudog.myulib.api.core.object.event;

import com.myudog.myulib.api.core.state.IState;

/**
 * Fired when the state of an object changes.
 * Completely decoupled from GameInstance.
 */
public record StateChangeEvent<S extends IState<?>>(
        Object source,
        S previousState,
        S newState
) {
}
'''
with open(sce_path, "w", encoding="utf-8") as f:
    f.write(new_sce)
print("Fixed StateChangeEvent.")

print("Done.")
