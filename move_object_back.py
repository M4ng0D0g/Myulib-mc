import os
import shutil

core_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib"
framework_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\main\java\com\myudog\myulib"

# We move api/framework/object back to api/core/object
src_path = os.path.join(framework_src, "api", "framework", "object")
dst_path = os.path.join(core_src, "api", "core", "object")

if os.path.exists(src_path):
    os.makedirs(os.path.dirname(dst_path), exist_ok=True)
    shutil.move(src_path, dst_path)

def replace_in_files(dir_path, old_str, new_str):
    for root, _, files in os.walk(dir_path):
        for file in files:
            if file.endswith(".java"):
                fpath = os.path.join(root, file)
                with open(fpath, "r", encoding="utf-8") as f:
                    content = f.read()
                if old_str in content:
                    content = content.replace(old_str, new_str)
                    with open(fpath, "w", encoding="utf-8") as f:
                        f.write(content)

replace_in_files(core_src, "com.myudog.myulib.api.framework.object", "com.myudog.myulib.api.core.object")
replace_in_files(framework_src, "com.myudog.myulib.api.framework.object", "com.myudog.myulib.api.core.object")

print("ObjectSystem moved back to core.")
