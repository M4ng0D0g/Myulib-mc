import os
import shutil

core_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib"
framework_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\main\java\com\myudog\myulib"

# List of specific files or directories to move
moves = [
    # (Source path relative to core_src, Dest path relative to framework_src, New Package Prefix)
    ("api/core/object", "api/framework/object", "com.myudog.myulib.api.framework.object"),
    ("api/core/ui", "api/framework/ui", "com.myudog.myulib.api.framework.ui"),
    ("api/core/events/StateTickEvent.java", "api/framework/game/core/event/StateTickEvent.java", "com.myudog.myulib.api.framework.game.core.event"),
    ("internal/game", "internal/game", "com.myudog.myulib.internal.game"),
    ("internal/field", "internal/field", "com.myudog.myulib.internal.field"),
    ("internal/team", "internal/team", "com.myudog.myulib.internal.team"),
    ("internal/permission", "internal/permission", "com.myudog.myulib.internal.permission"),
    ("internal/rolegroup", "internal/rolegroup", "com.myudog.myulib.internal.rolegroup")
]

# We also need to move mixins that have "Permission" or "GameMode" in name.
mixins_to_move = []
if os.path.exists(os.path.join(core_src, "mixin")):
    for f in os.listdir(os.path.join(core_src, "mixin")):
        if "Permission" in f or "GameMode" in f or "Interact" in f:
            mixins_to_move.append(("mixin/" + f, "mixin/" + f, "com.myudog.myulib.mixin"))

moves.extend(mixins_to_move)

# Execute Moves
moved_files = [] # Store tuple of (old_full_class_name, new_full_class_name)

def get_class_name_from_path(base_dir, path):
    rel = os.path.relpath(path, base_dir)
    return rel.replace("\\", ".").replace("/", ".").replace(".java", "")

for src_rel, dst_rel, new_pkg in moves:
    src_path = os.path.join(core_src, src_rel)
    dst_path = os.path.join(framework_src, dst_rel)
    
    if not os.path.exists(src_path):
        continue
        
    os.makedirs(os.path.dirname(dst_path), exist_ok=True)
    
    if os.path.isdir(src_path):
        for root, _, files in os.walk(src_path):
            for file in files:
                if file.endswith(".java"):
                    full_src = os.path.join(root, file)
                    rel_to_src_path = os.path.relpath(full_src, src_path)
                    full_dst = os.path.join(dst_path, rel_to_src_path)
                    os.makedirs(os.path.dirname(full_dst), exist_ok=True)
                    shutil.move(full_src, full_dst)
                    
                    old_class = get_class_name_from_path(core_src, full_src)
                    new_class = get_class_name_from_path(framework_src, full_dst)
                    moved_files.append((old_class, new_class, full_dst))
        # Remove empty dir
        shutil.rmtree(src_path, ignore_errors=True)
    else:
        shutil.move(src_path, dst_path)
        old_class = get_class_name_from_path(core_src, src_path)
        new_class = get_class_name_from_path(framework_src, dst_path)
        moved_files.append((old_class, new_class, dst_path))

print(f"Moved {len(moved_files)} files.")

# Step 2: Update packages in moved files
for old_class, new_class, full_dst in moved_files:
    new_pkg_str = ".".join(new_class.split(".")[:-1])
    with open(full_dst, "r", encoding="utf-8") as f:
        content = f.read()
    
    # Replace package declaration
    lines = content.split("\n")
    for i, line in enumerate(lines):
        if line.startswith("package "):
            lines[i] = f"package {new_pkg_str};"
            break
    
    with open(full_dst, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

# Step 3: Global replace of old_class imports with new_class across BOTH core and framework
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

print("Package and import updates completed.")
