import os
import shutil

core_mixin = r"d:\Minecraft\projects\MyuLib-mc\myulib-core\src\main\java\com\myudog\myulib\mixin"
framework_mixin = r"d:\Minecraft\projects\MyuLib-mc\myulib-framework\src\main\java\com\myudog\myulib\mixin"

os.makedirs(framework_mixin, exist_ok=True)

# Move permission-related and player-entity Mixins to framework
# (they reference PermissionGate, PermissionAction etc which live in framework)
files_to_move = [
    "MixinItemEntityPermission.java",
    "MixinFarmBlockPermission.java",
    "MixinPlayerDropPermission.java",
    "MixinServerMessagePermission.java",
    "MixinEntityPortalPermission.java",
    "MixinPressurePlatePermission.java",
    "MixinTripWirePermission.java",
    "MixinServerPlayerControlPermission.java",
    "MixinPlayerEntity.java",
    "MixinLivingEntity.java",  # also references PermissionGate/Decision
]

for f in files_to_move:
    src = os.path.join(core_mixin, f)
    dst = os.path.join(framework_mixin, f)
    if os.path.exists(src):
        shutil.move(src, dst)
        print(f"Moved {f} -> framework mixin")

print("Done.")
