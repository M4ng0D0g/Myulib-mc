package com.myudog.myulib.api.core.util;

import java.security.SecureRandom;

public final class ShortIdGenerator {
    // 使用 SecureRandom 確保密碼學級別的隨機性，避免偽隨機數生成器的規律
    private static final SecureRandom RANDOM = new SecureRandom();

    // 🌟 修正重點：拔除大寫字母！只留下小寫字母與數字 (Base36)
    // 這樣才能完美符合 Minecraft Identifier 的嚴格規範
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";

    // 預設長度設為 10
    private static final int DEFAULT_LENGTH = 10;

    private ShortIdGenerator() {}

    /**
     * 生成預設長度 (10碼) 的隨機短 ID
     */
    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    /**
     * 自訂長度的隨機短 ID
     */
    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}