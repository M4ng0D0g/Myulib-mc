package com.myudog.myulib.api.core.object;

import com.myudog.myulib.api.framework.game.core.GameInstance;
import net.minecraft.resources.Identifier;

/**
 * 遊戲物件藍圖 (Template / Config)
 * 絕對靜態、不可變，負責從設定檔讀取資料，並在遊戲開始時「生產」執行期物件。
 */
public interface IObjectDef {

    Identifier id();
    ObjectKind getKind();
    Identifier toIdentifier();

    /**
     * 驗證藍圖參數是否合法 (對應你目前的 validate 邏輯)
     */
    boolean validate();

    /**
     * 工廠方法：根據此藍圖，實例化出一個真正的遊戲物件
     * @param instance 歸屬的遊戲實例
     * @return 準備好進入生命週期的 IGameObject
     */
    IObjectRt createRuntimeInstance(GameInstance<?, ?, ?> instance);

    default IObjectRt spawn(GameInstance<?, ?, ?> instance) {
        return createRuntimeInstance(instance);
    }
}