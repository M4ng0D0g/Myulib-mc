import os
import re

ROOT = r"d:\Minecraft\projects\MyuLib-mc"

def fix_package_declarations(src_root, expected_prefix):
    """
    Walk all .java files under src_root.
    For each file, compute what the package SHOULD be based on its path,
    then replace the declared package with the correct one.
    """
    java_base = os.path.join(src_root, "src", "main", "java")
    client_base = os.path.join(src_root, "src", "client", "java")
    
    for base in [java_base, client_base]:
        if not os.path.exists(base):
            continue
        for root, _, files in os.walk(base):
            for file in files:
                if not file.endswith(".java"):
                    continue
                fpath = os.path.join(root, file)
                rel = os.path.relpath(fpath, base)  # e.g. com\myudog\myulib\api\core\object\event\BlockBreakEvent.java
                parts = rel.replace("\\", "/").split("/")[:-1]  # remove filename
                correct_pkg = ".".join(parts)
                
                with open(fpath, "r", encoding="utf-8") as f:
                    content = f.read()
                
                # Replace package declaration
                new_content = re.sub(
                    r"^package [^;]+;",
                    f"package {correct_pkg};",
                    content,
                    count=1,
                    flags=re.MULTILINE
                )
                
                if new_content != content:
                    with open(fpath, "w", encoding="utf-8") as f:
                        f.write(new_content)
                    print(f"Fixed package in: {rel}")

fix_package_declarations(os.path.join(ROOT, "myulib-core"), "com.myudog.myulib")
fix_package_declarations(os.path.join(ROOT, "myulib-framework"), "com.myudog.myulib")
print("Done.")
