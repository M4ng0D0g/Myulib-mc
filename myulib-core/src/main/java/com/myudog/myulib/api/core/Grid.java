package com.myudog.myulib.api.core;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.*;

@Deprecated
public class Grid<T> implements Iterable<T> {

    private final ArrayList<T> array;
    private final Map<T, Index> itemToPos;

    private final Size size;
    private final int stride;

    public Grid(@NotNull Size size) {
        assert size.getDimensionCount() == 2 :  "Dimension count must be 2";

        int[] shape = size.getShape();
        this.array = new ArrayList<>(shape[0] * shape[1]);
        this.itemToPos = new HashMap<>();

        this.size = size;
        this.stride = shape[1];
    }

    public Size getSize() { return size; }

    public T get(@NotNull Index index) {
        assert index.getDimensionCount() == 2 : "Index count must be 2";
        int[] shape = index.getShape();
        assert this.size.enclose(index) : "Index out of bounds: " + shape[0] + ", " + shape[1];

        return array.get(getIndex(index));
    }

    public Index getPos(T item) {
        if (item == null) return null;
        return itemToPos.get(item);
    }

    public Collection<T> getAllItems() {
        return itemToPos.keySet();
    }

    public void put(@NotNull Index index, T item) {
        assert index.getDimensionCount() == 2 : "Index count must be 2";
        int[] shape = index.getShape();
        assert this.size.enclose(index) : "Index out of bounds: " + shape[0] + ", " + shape[1];

        // 若傳入 null，等同於刪除該座標的物件
        if (item == null) {
            removeAt(index);
            return;
        }

        // 🛡️ 邏輯 1：如果該物件已經在棋盤的其他地方，先清除它的舊分身 (實現「移動」)
        Index oldPos = itemToPos.get(item);
        if (oldPos != null) {
            array.set(getIndex(oldPos), null);
        }

        // 🛡️ 邏輯 2：如果目標座標上已經有別的物件，將該倒霉鬼從 Map 中剔除 (實現「吃子」)
        T existingTarget = array.get(getIndex(index));
        if (existingTarget != null) {
            itemToPos.remove(existingTarget);
        }

        // 💡 邏輯 3：雙向寫入
        array.set(getIndex(index), item);
        itemToPos.put(item, index);
    }

    public T removeAt(@NotNull Index index) {
        assert index.getDimensionCount() == 2 : "Index count must be 2";
        int[] shape = index.getShape();
        assert this.size.enclose(index) : "Index out of bounds: " + shape[0] + ", " + shape[1];

        T existing = array.get(getIndex(index));

        if (existing != null) {
            array.set(getIndex(index), null);
            itemToPos.remove(existing);
        }
        return existing;
    }

    public void remove(T item) {
        Index index = itemToPos.remove(item);
        if (index != null) {
            array.set(getIndex(index), null);
        }
    }

    public void clear() {
        Collections.fill(this.array, null);
        itemToPos.clear();
    }

    // --- 輔助工具 ---

    private int getIndex(Index index) {
        assert index.getDimensionCount() == 2 : "Index count must be 2";

        int[] shape = index.getShape();
        return shape[1] * stride + shape[0]; // 二維轉一維的標準公式
    }

    @Override
    public @NonNull Iterator<T> iterator() {
        return itemToPos.keySet().iterator();
    }
}
