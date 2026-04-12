package com.myudog.myulib.api.game.core;

import com.myudog.myulib.api.core.ShortIdGenerator;
import com.myudog.myulib.api.game.object.IGameEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public abstract class GameData {

    private static final Set<String> USED_IDS = ConcurrentHashMap.newKeySet();
    public static final String PREFIX = "gd_";
    private final String id;

    private final Set<Integer> timerInstanceIds = new LinkedHashSet<>();
    private final Map<Integer, String> timerTags = new LinkedHashMap<>();

    // 🌟 修正：替換舊版 ObjectRuntime，改用 IGameEntity
    private final Set<IGameEntity> activeEntities = new LinkedHashSet<>();

    private final List<String> scoreboardLines = new ArrayList<>();
    private final Map<String, Integer> scoreboardValues = new LinkedHashMap<>();

    protected GameData() {
        this.id = generateUniqueId();
    }

    protected GameData(String existingId) {
        this.id = existingId;
        USED_IDS.add(existingId);
    }

    public String getId() {
        return this.id;
    }

    private static String generateUniqueId() {
        String newId;
        do {
            newId = PREFIX + ShortIdGenerator.generate(6);
        } while (!USED_IDS.add(newId));
        return newId;
    }

    // --- 記憶體管理 (供系統重載或關閉時呼叫) ---

    /**
     * 清空 ID 快取池。通常在伺服器關閉，或 /reload 重新讀取所有 NBT 前呼叫。
     */
    public static void clearIdCache() {
        USED_IDS.clear();
    }

    public void reset() {
        timerInstanceIds.clear();
        timerTags.clear();
        activeEntities.clear();
        scoreboardLines.clear();
        scoreboardValues.clear();
    }

    public final Set<Integer> timerInstanceIds() {
        return timerInstanceIds;
    }

    public final Map<Integer, String> timerTags() {
        return timerTags;
    }

    // 🌟 新增 Getter
    public final Set<IGameEntity> activeEntities() {
        return activeEntities;
    }

    public final List<String> scoreboardLines() {
        return scoreboardLines;
    }

    public final Map<String, Integer> scoreboardValues() {
        return scoreboardValues;
    }
}