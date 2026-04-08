# RegionSystem
## Role
This page is the canonical reference for `RegionSystem` in the `ui` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Field 系統使用說明（舊 region 名稱的過渡入口）

`field/` 是獨立於遊戲主狀態的長期空間系統，負責 AABB 區域、跨維度包裹、條件查詢，以及區域規則；`region/` 僅保留作舊名過渡。

## 系統入口
- [Field Core](../api/field/FieldCore.md)
- [Field](../field/index.md)

## 公開型別
- [`FieldBounds`](../api/field/FieldCore.md)
- [`FieldDefinition`](../api/field/FieldCore.md)
- [`FieldRole`](../api/field/FieldCore.md)
- [`FieldManager`](../api/field/FieldCore.md)
- [`FieldAdminService`](../api/field/FieldCore.md)

## 本系統適合做什麼
- 戰場邊界
- 準備區 / 出生區
- 區域限制行為
- 長期存在的系統空間管理

## 文件導覽
- 詳細 API：`docs/api/field/*.md`（舊 `region` API 僅保留作過渡相容）


