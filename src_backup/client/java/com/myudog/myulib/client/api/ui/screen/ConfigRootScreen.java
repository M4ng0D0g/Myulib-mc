package com.myudog.myulib.client.api.ui.screen;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myudog.myulib.api.core.ui.network.ConfigUiNetworking;
import com.myudog.myulib.client.api.ui.network.ConfigUiClientState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ConfigRootScreen extends Screen {
    private static final Gson GSON = new Gson();

    private final boolean clientReadonlyHint;
    private int observedRevision = -1;

    private ViewMode viewMode = ViewMode.ROLE_GROUP;
    private final List<RoleGroupItem> roleGroups = new ArrayList<>();
    private int selectedRoleGroup = -1;

    private final List<String> globalGroups = new ArrayList<>();
    private final List<String> dimensions = new ArrayList<>();
    private final List<String> fields = new ArrayList<>();
    private ScopeTab scopeTab = ScopeTab.GLOBAL;

    private boolean dirty;
    private String baselineSnapshot = "{}";

    public ConfigRootScreen(boolean clientReadonlyHint) {
        super(Component.literal("MyuLib Config"));
        this.clientReadonlyHint = clientReadonlyHint;
    }

    @Override
    protected void init() {
        reloadFromState();
        rebuildWidgets();
    }

    @Override
    public void tick() {
        super.tick();
        if (observedRevision != ConfigUiClientState.revision() && !dirty) {
            reloadFromState();
            rebuildWidgets();
        }
    }

    @Override
    protected void rebuildWidgets() {
        clearWidgets();

        int panelWidth = Math.min(520, this.width - 24);
        int panelHeight = Math.min(320, this.height - 24);
        int panelX = (this.width - panelWidth) / 2;
        int panelY = (this.height - panelHeight) / 2;

        int leftWidth = 130;
        int rightX = panelX + leftWidth + 16;
        int rightWidth = panelWidth - leftWidth - 24;

        addRenderableWidget(Button.builder(Component.literal("身分組"), button -> {
            viewMode = ViewMode.ROLE_GROUP;
            rebuildWidgets();
        }).bounds(panelX + 8, panelY + 30, leftWidth - 16, 20).build());

        addRenderableWidget(Button.builder(Component.literal("權限"), button -> {
            viewMode = ViewMode.PERMISSION;
            rebuildWidgets();
        }).bounds(panelX + 8, panelY + 56, leftWidth - 16, 20).build());

        if (viewMode == ViewMode.ROLE_GROUP) {
            buildRoleGroupWidgets(rightX, panelY + 30, rightWidth);
        } else {
            buildPermissionWidgets(rightX, panelY + 30, rightWidth);
        }

        int confirmY = panelY + panelHeight - 34;
        if (dirty) {
            addRenderableWidget(Button.builder(Component.literal("回復原狀"), button -> {
                reloadFromState();
                dirty = false;
                rebuildWidgets();
            }).bounds(panelX + 12, confirmY, 110, 20).build());

            addRenderableWidget(Button.builder(Component.literal("保存"), button -> {
                sendApplyMutation();
                dirty = false;
                rebuildWidgets();
            }).bounds(panelX + panelWidth - 122, confirmY, 110, 20).build());
        }
    }

    private void buildRoleGroupWidgets(int x, int y, int width) {
        int rowY = y;
        int index = 0;
        for (RoleGroupItem group : roleGroups) {
            final int itemIndex = index;
            boolean selected = selectedRoleGroup == itemIndex;
            String suffix = selected ? " <" : "";
            String label = group.path + " (p=" + group.priority + ")" + suffix;
            addRenderableWidget(Button.builder(Component.literal(label), button -> {
                selectedRoleGroup = itemIndex;
                rebuildWidgets();
            }).bounds(x, rowY, width - 48, 20).build());

            addRenderableWidget(Button.builder(Component.literal("="), button -> selectedRoleGroup = itemIndex)
                    .bounds(x + width - 44, rowY, 20, 20)
                    .build());

            addRenderableWidget(Button.builder(Component.literal("^"), button -> {
                moveRoleGroup(itemIndex, -1);
            }).bounds(x + width - 22, rowY, 20, 10).build());

            addRenderableWidget(Button.builder(Component.literal("v"), button -> {
                moveRoleGroup(itemIndex, 1);
            }).bounds(x + width - 22, rowY + 10, 20, 10).build());

            rowY += 22;
            index++;
            if (rowY > this.height - 70) {
                break;
            }
        }
    }

    private void buildPermissionWidgets(int x, int y, int width) {
        addRenderableWidget(Button.builder(Component.literal("global"), button -> {
            scopeTab = ScopeTab.GLOBAL;
            rebuildWidgets();
        }).bounds(x, y, 70, 20).build());
        addRenderableWidget(Button.builder(Component.literal("dimension"), button -> {
            scopeTab = ScopeTab.DIMENSION;
            rebuildWidgets();
        }).bounds(x + 74, y, 84, 20).build());
        addRenderableWidget(Button.builder(Component.literal("field"), button -> {
            scopeTab = ScopeTab.FIELD;
            rebuildWidgets();
        }).bounds(x + 162, y, 70, 20).build());

        int rowY = y + 26;
        List<String> source = switch (scopeTab) {
            case GLOBAL -> globalGroups;
            case DIMENSION -> dimensions;
            case FIELD -> fields;
        };

        for (String line : source) {
            addRenderableWidget(Button.builder(Component.literal(line), button -> {
            }).bounds(x, rowY, width, 20).build());
            rowY += 22;
            if (rowY > this.height - 70) {
                break;
            }
        }
    }

    private void moveRoleGroup(int index, int delta) {
        int target = index + delta;
        if (index < 0 || index >= roleGroups.size() || target < 0 || target >= roleGroups.size()) {
            return;
        }
        RoleGroupItem current = roleGroups.remove(index);
        roleGroups.add(target, current);

        int priority = roleGroups.size();
        for (int i = 0; i < roleGroups.size(); i++) {
            RoleGroupItem old = roleGroups.get(i);
            roleGroups.set(i, new RoleGroupItem(old.id, old.path, priority--));
        }
        selectedRoleGroup = target;
        dirty = true;
        rebuildWidgets();
    }

    private void sendApplyMutation() {
        JsonObject mutation = new JsonObject();
        JsonArray order = new JsonArray();
        for (RoleGroupItem item : roleGroups) {
            order.add(item.id);
        }
        mutation.add("roleGroupOrder", order);

        ClientPlayNetworking.send(new ConfigUiNetworking.ConfigApplyPayload(GSON.toJson(mutation)));
        ClientPlayNetworking.send(new ConfigUiNetworking.ConfigSnapshotRequestPayload());
    }

    private void reloadFromState() {
        observedRevision = ConfigUiClientState.revision();
        baselineSnapshot = ConfigUiClientState.snapshotJson();

        roleGroups.clear();
        globalGroups.clear();
        dimensions.clear();
        fields.clear();

        JsonObject root = parseObject(baselineSnapshot);
        JsonArray roleArray = root.getAsJsonArray("roleGroups");
        if (roleArray != null) {
            for (JsonElement element : roleArray) {
                if (!(element instanceof JsonObject item)) {
                    continue;
                }
                roleGroups.add(new RoleGroupItem(
                        item.get("id").getAsString(),
                        item.get("path").getAsString(),
                        item.get("priority").getAsInt()
                ));
            }
            roleGroups.sort(Comparator.comparingInt(RoleGroupItem::priority).reversed());
        }

        JsonObject permissions = root.getAsJsonObject("permissions");
        if (permissions != null) {
            JsonArray globals = permissions.getAsJsonArray("globalGroups");
            if (globals != null) {
                for (JsonElement global : globals) {
                    globalGroups.add(global.getAsString());
                }
            }

            JsonArray dims = permissions.getAsJsonArray("dimensions");
            if (dims != null) {
                for (JsonElement dim : dims) {
                    if (dim instanceof JsonObject obj) {
                        dimensions.add(obj.get("id").getAsString());
                    }
                }
            }

            JsonArray fieldArray = permissions.getAsJsonArray("fields");
            if (fieldArray != null) {
                for (JsonElement field : fieldArray) {
                    if (field instanceof JsonObject obj) {
                        fields.add(obj.get("name").getAsString() + " (" + obj.get("id").getAsString() + ")");
                    }
                }
            }
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        extractBackground(graphics, mouseX, mouseY, partialTick);

        int panelWidth = Math.min(520, this.width - 24);
        int panelHeight = Math.min(320, this.height - 24);
        int panelX = (this.width - panelWidth) / 2;
        int panelY = (this.height - panelHeight) / 2;

        graphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xD0181B22);
        graphics.fill(panelX + 130, panelY + 24, panelX + 132, panelY + panelHeight - 40, 0x90FFFFFF);

        graphics.text(this.font, this.title, panelX + 8, panelY + 8, 0xFFFFFF, false);

        boolean readonly = clientReadonlyHint || ConfigUiClientState.readonly();
        if (readonly) {
            graphics.text(this.font, Component.literal("唯讀模式：需 OP 且創造模式"), panelX + 150, panelY + 8, 0xFF8888, false);
        }

        if (dirty) {
            int barY = panelY + panelHeight - 56;
            graphics.fill(panelX + 8, barY, panelX + panelWidth - 8, barY + 18, 0xD0503A20);
            graphics.text(this.font, Component.literal("有未保存變更，請保存或回復原狀"), panelX + 12, barY + 5, 0xFFF2C08C, false);
        } else {
            String last = ConfigUiClientState.lastApplyMessage();
            if (last != null && !last.isBlank()) {
                graphics.text(this.font, Component.literal(last), panelX + 12, panelY + panelHeight - 24, 0xA0FFFFFF, false);
            }
        }

        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !dirty;
    }

    @Override
    public void onClose() {
        if (dirty) {
            return;
        }
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean canInterruptWithAnotherScreen() {
        return !dirty;
    }

    public static void openForCurrentPlayer(boolean clientReadonlyHint) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return;
        }
        ClientPlayNetworking.send(new ConfigUiNetworking.ConfigSnapshotRequestPayload());
        client.setScreen(new ConfigRootScreen(clientReadonlyHint));
    }

    private static JsonObject parseObject(String raw) {
        if (raw == null || raw.isBlank()) {
            return new JsonObject();
        }
        JsonElement parsed = GSON.fromJson(raw, JsonElement.class);
        if (parsed == null || !parsed.isJsonObject()) {
            return new JsonObject();
        }
        return parsed.getAsJsonObject();
    }

    private enum ViewMode {
        ROLE_GROUP,
        PERMISSION
    }

    private enum ScopeTab {
        GLOBAL,
        DIMENSION,
        FIELD
    }

    private record RoleGroupItem(String id, String path, int priority) {
    }
}


