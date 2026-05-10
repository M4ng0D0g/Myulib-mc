package com.myudog.myulib.api.core;

import org.jetbrains.annotations.NotNull;

/**
 * 具備路徑合併能力的 Token 實作。
 */
public record Token(@NotNull String path, @NotNull String value) implements Tokenable {

    /**
     * 核心構造函數：確保路徑格式統一，去除首尾空白與多餘斜線。
     */
    public Token {
        path = path.replaceAll("^/|/$", "");
        value = value.trim();
    }

    /**
     * 變長參數構造函數：將最後一個參數視為 value，其餘部分合併為 path。
     * 範例：new Token("game", "room1", "team1") -> path: "game/room1", value: "team1"
     */
    public Token(@NotNull String... args) {
        this(
                args.length > 1 ? String.join("/", java.util.Arrays.copyOf(args, args.length - 1)) : "",
                args.length > 0 ? args[args.length - 1] : ""
        );
    }

    /**
     * Token 嵌套構造函數：實現「路徑繼承」。
     * 將父 Token 的完整標識（path + value）作為子 Token 的路徑前綴。
     * 範例：
     * parent = new Token("game", "room1")
     * child = new Token(parent, "red_team") -> path: "game/room1", value: "red_team"
     */
    public Token(@NotNull Tokenable parent, @NotNull String value) {
        this(
                parent.getPath().isEmpty() ? parent.getToken() : parent.getPath() + "/" + parent.getToken(),
                value
        );
    }

    @Override
    public @NotNull String getPath() {
        return path;
    }

    @Override
    public @NotNull String getToken() {
        return value;
    }
}