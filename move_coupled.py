import os
import shutil

core_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib"
framework_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\main\java\com\myudog\myulib"

moved_files = []

def get_class_name(base, path):
    return os.path.relpath(path, base).replace("\\", ".").replace("/", ".").replace(".java", "")

# 1. Find all files in core that import framework
for root, _, files in os.walk(core_src):
    for file in files:
        if file.endswith(".java"):
            full_path = os.path.join(root, file)
            with open(full_path, "r", encoding="utf-8") as f:
                content = f.read()
            if "import com.myudog.myulib.api.framework" in content or "import com.myudog.myulib.internal.game" in content:
                # Move this file to framework!
                rel_path = os.path.relpath(full_path, core_src)
                
                # If it was in api/core, rename to api/framework
                dst_rel = rel_path.replace("api\\core\\", "api\\framework\\").replace("api/core/", "api/framework/")
                dst_path = os.path.join(framework_src, dst_rel)
                
                os.makedirs(os.path.dirname(dst_path), exist_ok=True)
                shutil.move(full_path, dst_path)
                
                old_class = get_class_name(core_src, full_path)
                new_class = get_class_name(framework_src, dst_path)
                moved_files.append((old_class, new_class, dst_path))

print(f"Moved {len(moved_files)} additional coupled files.")

# 2. Update package declarations
for old_class, new_class, full_dst in moved_files:
    new_pkg_str = ".".join(new_class.split(".")[:-1])
    with open(full_dst, "r", encoding="utf-8") as f:
        content = f.read()
    
    lines = content.split("\n")
    for i, line in enumerate(lines):
        if line.startswith("package "):
            lines[i] = f"package {new_pkg_str};"
            break
            
    with open(full_dst, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

# 3. Global replace imports
root_dirs = [
    r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src",
    r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src"
]

for d in root_dirs:
    for root, _, files in os.walk(d):
        for file in files:
            if file.endswith(".java"):
                fpath = os.path.join(root, file)
                with open(fpath, "r", encoding="utf-8") as f:
                    content = f.read()
                
                changed = False
                for old_class, new_class, _ in moved_files:
                    if old_class in content:
                        content = content.replace(old_class, new_class)
                        changed = True
                
                if changed:
                    with open(fpath, "w", encoding="utf-8") as f:
                        f.write(content)

print("Packages updated.")
