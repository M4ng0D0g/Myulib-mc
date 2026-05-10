package com.myudog.myulib.api.core;

import com.myudog.myulib.Myulib;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 最小化識別碼介面。
 * 具有 token 與 path，並提供統一的 toIdentifier 實作。
 */
public interface Tokenable {
    /** 獲取物件本身的標籤 (例如 "red_team") */
    @NotNull String getToken();

    /** 獲取物件所屬的層級路徑 (例如 "game/room_1") */
    @NotNull String getPath();

    /**
     * 統一的 Identifier 建立方式，強制使用 Myulib.MOD_ID 作為命名空間。
     */
    default Identifier toIdentifier() {
        return Myulib.id(getPath() + "/" + getToken());
    }
}