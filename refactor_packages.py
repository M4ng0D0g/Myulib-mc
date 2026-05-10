import os
import shutil
import re

base_dir = r"d:\Minecraft\projects\MyuLib-mc\src"
framework_modules = ["game", "team", "rolegroup", "permission", "field"]

def update_file_contents(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Update package and import declarations
    # Original: com.myudog.myulib.api.MODULE
    
    def replacer(match):
        prefix = match.group(1) # package or import
        module = match.group(2)
        rest = match.group(3)
        if module in framework_modules:
            return f"{prefix} com.myudog.myulib.api.framework.{module}{rest}"
        elif module not in ['framework', 'core', 'AccessSystems', 'MyulibApi', 'MyuVFX', 'MyuVFXManager', 'Shapes', 'VFXCompat']:
            return f"{prefix} com.myudog.myulib.api.core.{module}{rest}"
        return match.group(0)

    # Regex to match 'package com.myudog.myulib.api.XXX;' or 'import com.myudog.myulib.api.XXX.YYY;'
    new_content = re.sub(r'(package|import)\s+com\.myudog\.myulib\.api\.([a-z0-9_]+)(.*?);', replacer, content)
    
    if new_content != content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def process_directory(src_set_dir):
    api_dir = os.path.join(src_set_dir, 'com', 'myudog', 'myulib', 'api')
    if not os.path.exists(api_dir):
        return

    framework_dir = os.path.join(api_dir, 'framework')
    core_dir = os.path.join(api_dir, 'core')

    os.makedirs(framework_dir, exist_ok=True)
    os.makedirs(core_dir, exist_ok=True)

    # Move directories
    for item in os.listdir(api_dir):
        if item in ['framework', 'core']:
            continue
            
        item_path = os.path.join(api_dir, item)
        if os.path.isdir(item_path):
            if item in framework_modules:
                shutil.move(item_path, os.path.join(framework_dir, item))
            else:
                shutil.move(item_path, os.path.join(core_dir, item))

def update_all_files():
    count = 0
    for root, dirs, files in os.walk(base_dir):
        for file in files:
            if file.endswith('.java') or file.endswith('.kt') or file.endswith('.md') or file.endswith('.mmd'):
                file_path = os.path.join(root, file)
                try:
                    if update_file_contents(file_path):
                        count += 1
                except Exception as e:
                    pass
    print(f"Updated {count} files.")

def main():
    print("Moving directories...")
    # Apply to main, client, test
    for src_set in ['main', 'client', 'test']:
        src_set_dir = os.path.join(base_dir, src_set, 'java')
        process_directory(src_set_dir)
        
    print("Updating file contents...")
    update_all_files()
    print("Refactor complete.")

if __name__ == '__main__':
    main()
