import os
import re

base_dir = r"d:\Minecraft\projects\MyuLib-mc\src"

def fix_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find lines starting with 'package' or 'import' that don't end with ';'
    lines = content.split('\n')
    changed = False
    for i, line in enumerate(lines):
        # check if line starts with package or import and has no semicolon at the end
        if line.startswith('package ') or line.startswith('import '):
            stripped = line.rstrip()
            if len(stripped) > 0 and not stripped.endswith(';'):
                # Add semicolon
                lines[i] = stripped + ';'
                changed = True
                
    if changed:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write('\n'.join(lines))
        return True
    return False

count = 0
for root, dirs, files in os.walk(base_dir):
    for file in files:
        if file.endswith('.java'):
            file_path = os.path.join(root, file)
            if fix_file(file_path):
                count += 1
                
print(f"Fixed {count} files.")
