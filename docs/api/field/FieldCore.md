# Field Core API 參考

## 型別
- `FieldBounds`
- `FieldDefinition`
- `FieldRole`
- `FieldManager`
- `FieldAdminService`

## 主要方法
- `FieldManager.register(...)` / `FieldManager.unregister(...)` / `FieldManager.get(...)`
- `FieldManager.update(...)` / `FieldManager.findAt(...)` / `FieldManager.all()`
- `FieldAdminService.create(...)` / `FieldAdminService.delete(...)` / `FieldAdminService.update(...)`
- `FieldAdminService.openEditor(...)`

## 簡述
`Field` 是獨立於 `game` 的世界場域系統，取代舊的 `region` 命名。

## 設定入口
Command 與 UI 請共用 `FieldAdminService` 與 `ConfigurationUiBridge`，避免各自維護一套設定流程。

