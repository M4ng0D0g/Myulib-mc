package com.myudog.myulib.api.game.core;

import com.myudog.myulib.api.game.object.IGameObject;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.Objects;

/**
 * [C] 遊戲設定藍圖 (Game Config)
 * 代表一局遊戲初始化所需的絕對靜態參數。
 * ⚠️ 必須保證為不可變 (Immutable)，且在遊戲實例建立後不得修改。
 */
public interface GameConfig {



    /**
     * 驗證設定參數是否合法。
     * 若不合法，請拋出帶有具體錯誤訊息的 IllegalArgumentException，
     * 這樣外部的指令系統 (Command) 就能直接捕捉並回傳提示給玩家。
     *
     * @throws IllegalArgumentException 若參數不符合遊戲啟動條件
     */
    default void validate() throws IllegalArgumentException {
        Map<Identifier, IGameObject> objects = Objects.requireNonNull(gameObjects(), "遊戲物件映射不得為空");
        if (objects.isEmpty()) throw new IllegalArgumentException("遊戲物件清單不得為空");

        for (Map.Entry<Identifier, IGameObject> entry : objects.entrySet()) {
            Identifier key = Objects.requireNonNull(entry.getKey(), "gameObjects key 不得為空");
            IGameObject template = Objects.requireNonNull(entry.getValue(), "gameObjects value 不得為空: " + key);

            if (!key.equals(template.getId())) {
                throw new IllegalArgumentException("gameObjects key 與物件 id 不一致: key=" + key + ", objectId=" + template.getId());
            }

            if (!template.validate()) {
                throw new IllegalArgumentException("遊戲物件模板驗證失敗: " + key);
            }

            IGameObject copy = Objects.requireNonNull(template.copy(), "遊戲物件 copy() 不得回傳 null: " + key);
            if (!copy.validate()) {
                throw new IllegalArgumentException("遊戲物件副本驗證失敗: " + key);
            }
        }
    }

    /**
     * 定義這局遊戲必須載入的遊戲物件藍圖 (例如：要在場上生成的自訂生物/棋子)。
     * 這些通常是在玩家輸入指令時，由指令層級預先定義好的。
     */
    Map<Identifier, IGameObject> gameObjects();

    /**
     * 提供一個預設的空設定，適用於完全不需要外部參數的簡單遊戲。
     */
    static GameConfig empty() {
        return new GameConfig() {
            @Override
            public Map<Identifier, IGameObject> gameObjects() {
                return Map.of();
            }
        };
    }
}