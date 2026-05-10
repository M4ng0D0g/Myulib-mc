import os
import shutil
import re

core_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib\mixin"
framework_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\main\java\com\myudog\myulib\mixin"

os.makedirs(core_src, exist_ok=True)

if os.path.exists(framework_src):
    for f in os.listdir(framework_src):
        if f.endswith(".java"):
            src_file = os.path.join(framework_src, f)
            dst_file = os.path.join(core_src, f)
            shutil.move(src_file, dst_file)
            
            with open(dst_file, "r", encoding="utf-8") as file:
                content = file.read()
                
            # Replace GameManager with ObjectManager for event handling
            content = content.replace("import com.myudog.myulib.api.framework.game.core.GameManager;", "import com.myudog.myulib.api.core.object.ObjectManager;")
            content = content.replace("GameManager.INSTANCE.handle", "ObjectManager.INSTANCE.handle")
            
            # For VFX Manager in MixinLivingEntity
            content = content.replace("GameManager.INSTANCE.getGlobalEffectManager()", "com.myudog.myulib.api.core.vfx.VFXManager.INSTANCE")
            
            with open(dst_file, "w", encoding="utf-8") as file:
                file.write(content)

print("Mixins moved back to core and updated.")
