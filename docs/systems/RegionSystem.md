# Region 系統使用說明（舊名／過渡入口）

`region/` 是舊名；新主線請看 `docs/field/`。Field 是獨立於遊戲主狀態的長期空間系統，負責 AABB 區域、跨維度查詢，以及區域規則。

## 系統入口
- [Field Core](../api/field/FieldCore.md)
- [Field](../field/index.md)

## 公開型別
- [`FieldBounds`](../api/field/FieldCore.md)
- [`FieldDefinition`](../api/field/FieldCore.md)
- [`FieldRole`](../api/field/FieldCore.md)
- [`FieldManager`](../api/field/FieldCore.md)
- [`FieldAdminService`](../api/field/FieldCore.md)

### 本系統適合做什麼
- 戰場邊界
- 準備區 / 出生區
- 區域限制行為
- 長期存在的系統空間管理

## 文件導覽
- 詳細 API：`docs/api/region/*.md`


