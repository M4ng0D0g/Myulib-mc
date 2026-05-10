package com.myudog.myulib.api.core.ui.sidebar;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 具備「零閃爍 (Zero-Flicker)」機制的虛擬個人計分板
 */
public class VirtualSidebar {
    private final ServerPlayer player;
    private final String objectiveName;
    private Component title;

    // 🌟 快取狀態：記錄當前畫面上實際有幾行，用來做差異比對與清除
    private int currentLineCount = 0;
    private boolean isVisible = false;

    public VirtualSidebar(ServerPlayer player, String objectiveName, Component title) {
        this.player = player;
        this.objectiveName = objectiveName;
        this.title = title;
    }

    public void show() {
        if (isVisible) return;

        Objective dummyObjective = new Objective(
                null, this.objectiveName, ObjectiveCriteria.DUMMY, this.title, ObjectiveCriteria.RenderType.INTEGER, false, null
        );
        player.connection.send(new ClientboundSetObjectivePacket(dummyObjective, 0)); // Action 0: Create

        // 1.20.2+ 使用 DisplaySlot Enum
        player.connection.send(new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, dummyObjective));

        isVisible = true;
    }

    public void hide() {
        if (!isVisible) return;

        Objective dummyObjective = new Objective(null, this.objectiveName, ObjectiveCriteria.DUMMY, this.title, ObjectiveCriteria.RenderType.INTEGER, false, null);
        player.connection.send(new ClientboundSetObjectivePacket(dummyObjective, 1)); // Action 1: Remove

        isVisible = false;
        currentLineCount = 0; // 重置狀態
    }

    public void updateTitle(Component newTitle) {
        this.title = newTitle;
        if (!isVisible) return;

        Objective dummyObjective = new Objective(null, this.objectiveName, ObjectiveCriteria.DUMMY, this.title, ObjectiveCriteria.RenderType.INTEGER, false, null);
        player.connection.send(new ClientboundSetObjectivePacket(dummyObjective, 2)); // Action 2: Update
    }

    /**
     * 🌟 零閃爍的核心更新演算法
     */
    public void updateLines(List<Component> newLines) {
        if (!isVisible) return;

        int newSize = newLines.size();

        // 1. 更新或新增行數
        for (int i = 0; i < newSize; i++) {
            // 使用固定的內部 ID (例如 "line_00", "line_01")
            String slotId = String.format("line_%02d", i);

            // 計算在畫面上的分數排序 (越上面分數越高，確保由上往下排)
            int scoreValue = 15 - i;

            // 🌟 1.20.3+ 專屬封包：指定 slotId，但顯示 newLines.get(i) 的文字！
            // ClientboundSetScorePacket(玩家名稱/ID, 計分板名稱, 分數, 顯示文字, 數字格式)
            player.connection.send(new ClientboundSetScorePacket(
                    slotId,
                    this.objectiveName,
                    scoreValue,
                    Optional.of(newLines.get(i)),
                    null
            ));
        }

        // 2. 清除多餘的舊行數 (Delta Cleanup)
        // 假設原本有 5 行，這次更新只傳入了 3 行，我們必須把舊的 line_03 和 line_04 刪除掉
        if (currentLineCount > newSize) {
            for (int i = newSize; i < currentLineCount; i++) {
                String obsoleteSlotId = String.format("line_%02d", i);
                // 傳送 Reset 封包來抹除這個特定的 ID
                player.connection.send(new ClientboundResetScorePacket(obsoleteSlotId, this.objectiveName));
            }
        }

        // 3. 更新快取狀態
        currentLineCount = newSize;
    }
}