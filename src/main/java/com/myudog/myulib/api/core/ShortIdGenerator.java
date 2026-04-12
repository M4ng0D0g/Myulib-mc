package com.myudog.myulib.api.core;

import java.security.SecureRandom;

public final class ShortIdGenerator {
    // 使用 SecureRandom 確保密碼學級別的隨機性，避免偽隨機數生成器的規律
    private static final SecureRandom RANDOM = new SecureRandom();
    // Base62 字元集：去除容易混淆的符號，對 URL 與 NBT 儲存都很友善
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // 預設長度設為 10。在 Base62 下，10 個字元有 62^10 (約 8.39 * 10^17) 種組合
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