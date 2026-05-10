import os
import shutil

root_dir = r"d:\Minecraft\projects\MyuLib-mc"
src_dir = os.path.join(root_dir, "src")

core_dir = os.path.join(root_dir, "myulib-core")
framework_dir = os.path.join(root_dir, "myulib-framework")

def copy_structure(src_base, dest_base, path_filter=None):
    for root, dirs, files in os.walk(src_base):
        for file in files:
            file_path = os.path.join(root, file)
            rel_path = os.path.relpath(file_path, src_base)
            
            if path_filter and not path_filter(rel_path):
                continue
                
            dest_path = os.path.join(dest_base, rel_path)
            os.makedirs(os.path.dirname(dest_path), exist_ok=True)
            shutil.copy2(file_path, dest_path)

def main():
    if not os.path.exists(core_dir):
        os.makedirs(core_dir)
    if not os.path.exists(framework_dir):
        os.makedirs(framework_dir)
        
    print("Moving core files...")
    # Core gets everything EXCEPT framework package
    def is_core(rel_path):
        normalized = rel_path.replace("\\", "/")
        if "api/framework" in normalized:
            return False
        # Mixins usually go to core for now unless they are specifically framework
        return True
        
    copy_structure(src_dir, os.path.join(core_dir, "src"), is_core)
    
    print("Moving framework files...")
    # Framework gets ONLY framework package
    def is_framework(rel_path):
        normalized = rel_path.replace("\\", "/")
        if "api/framework" in normalized:
            return True
        # Keep a basic structure if needed, but only copy framework java files
        return False
        
    copy_structure(src_dir, os.path.join(framework_dir, "src"), is_framework)

    print("File movement complete.")

if __name__ == '__main__':
    main()
