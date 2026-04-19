package com.myudog.myulib.api.team;

import net.minecraft.ChatFormatting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class TeamColorTest {

    @Test
    void teamColorExposesVanillaFormattingAndRgb() {
        assertEquals(ChatFormatting.RED, TeamColor.RED.toChatFormatting());
        assertEquals(ChatFormatting.DARK_BLUE, TeamColor.DARK_BLUE.toChatFormatting());
        assertEquals(0xFF5555, TeamColor.RED.rgb());
        assertEquals(0x0000AA, TeamColor.DARK_BLUE.rgb());
    }
}

