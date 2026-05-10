import os
import re

core_src = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib\api\core\object"

def refactor_file(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    # Remove the import for GameInstance
    content = re.sub(r"import com\.myudog\.myulib\.api\.framework\.game\.core\.GameInstance;\n?", "", content)
    content = re.sub(r"import com\.myudog\.myulib\.api\.framework\.game\.core\.GameManager;\n?", "", content)

    # 1. IObjectDef / BaseObjDef
    content = re.sub(r"IObjectRt createRuntimeInstance\(\s*GameInstance<\?,\s*\?,\s*\?>\s*\w*\s*\)", r"IObjectRt createRuntimeInstance()", content)
    content = re.sub(r"IObjectRt spawn\(\s*GameInstance<\?,\s*\?,\s*\?>\s*\w*\s*\)", r"IObjectRt spawn()", content)
    
    # 2. IObjectRt / BaseObjRt
    content = re.sub(r"void onInitialize\(\s*GameInstance<\?,\s*\?,\s*\?>\s*\w*\s*\)", r"void onInitialize()", content)
    content = re.sub(r"void spawn\(\s*GameInstance<\?,\s*\?,\s*\?>\s*\w*\s*\)", r"void spawn()", content)
    content = re.sub(r"void destroy\(\s*GameInstance<\?,\s*\?,\s*\?>\s*\w*\s*\)", r"void destroy()", content)
    content = re.sub(r"void onSpawn\(\s*GameInstance<\?,\s*\?,\s*\?>\s*\w*\s*\)", r"void onSpawn()", content)
    content = re.sub(r"void onDestroy\(\s*GameInstance<\?,\s*\?,\s*\?>\s*\w*\s*\)", r"void onDestroy()", content)
    
    # 3. IObjectBeh / ObjectBehaviorBinder / MineableBeh / etc.
    content = re.sub(r",\s*GameInstance<\?,\s*\?,\s*\?>\s*\w*", "", content)

    # 4. Method calls fix inside BaseObjRt etc.
    # spawn(instance) -> spawn()
    content = re.sub(r"spawn\(instance\)", r"spawn()", content)
    content = re.sub(r"destroy\(instance\)", r"destroy()", content)
    content = re.sub(r"onInitialize\(instance\)", r"onInitialize()", content)
    content = re.sub(r"onSpawn\(instance\)", r"onSpawn()", content)
    content = re.sub(r"onDestroy\(instance\)", r"onDestroy()", content)
    
    # ObjectBehaviorBinder.attach(object, instance) -> ObjectBehaviorBinder.attach(object)
    content = re.sub(r"ObjectBehaviorBinder\.attach\(this,\s*instance\)", r"ObjectBehaviorBinder.attach(this)", content)
    content = re.sub(r"ObjectBehaviorBinder\.detach\(this,\s*instance\)", r"ObjectBehaviorBinder.detach(this)", content)

    # 5. Fix remaining "GameInstance<?, ?, ?> instance" string literals
    content = re.sub(r"GameInstance<\?,\s*\?,\s*\?>\s*instance", "", content)

    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)

for root, _, files in os.walk(core_src):
    for file in files:
        if file.endswith(".java"):
            refactor_file(os.path.join(root, file))

print("ObjectSystem interfaces refactored.")
