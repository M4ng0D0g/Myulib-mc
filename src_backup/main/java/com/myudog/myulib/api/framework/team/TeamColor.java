package com.myudog.myulib.api.framework.team;

import net.minecraft.ChatFormatting;

public enum TeamColor implements com.myudog.myulib.api.core.Color {
    DEFAULT(ChatFormatting.RESET, 0xFFFFFF),
    RED(ChatFormatting.RED, 0xFF5555),
    BLUE(ChatFormatting.BLUE, 0x5555FF),
    GREEN(ChatFormatting.GREEN, 0x55FF55),
    YELLOW(ChatFormatting.YELLOW, 0xFFFF55),
    AQUA(ChatFormatting.AQUA, 0x55FFFF),
    WHITE(ChatFormatting.WHITE, 0xFFFFFF),
    BLACK(ChatFormatting.BLACK, 0x000000),
    GRAY(ChatFormatting.GRAY, 0xAAAAAA),
    GOLD(ChatFormatting.GOLD, 0xFFAA00),
    LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE, 0xFF55FF),
    DARK_PURPLE(ChatFormatting.DARK_PURPLE, 0xAA00AA),
    DARK_RED(ChatFormatting.DARK_RED, 0xAA0000),
    DARK_GREEN(ChatFormatting.DARK_GREEN, 0x00AA00),
    DARK_AQUA(ChatFormatting.DARK_AQUA, 0x00AAAA),
    DARK_BLUE(ChatFormatting.DARK_BLUE, 0x0000AA),
    DARK_GRAY(ChatFormatting.DARK_GRAY, 0x555555);

    private final ChatFormatting formatting;
    private final int rgb;

    TeamColor(ChatFormatting formatting, int rgb) {
        this.formatting = formatting;
        this.rgb = rgb;
    }

    public ChatFormatting toChatFormatting() {
        return formatting;
    }

    @Override
    public int rgb() {
        return rgb;
    }
}
