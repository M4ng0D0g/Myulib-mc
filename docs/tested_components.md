# 已測試元件清單（開發者標記）

此文件提供一個可勾選的 Markdown 檢查表，用於標記哪些 UI 元件/Node 已由開發者在本地手動測試。與系統內部狀態分離：標記僅存在於此檔案，方便追蹤測試覆蓋情況。

使用說明
- 開發者在本地測試過某個元件後，請在此檔案中對應項目打勾（將 `- [ ]` 改為 `- [x]`）。
- 檔案可加入版本控制，用於團隊共享測試進度。

目錄（依功能分類）

Containers
- [ ] Box
- [ ] Panel
- [ ] ScrollBox
- [ ] Grid
- [ ] Stack
- [ ] Canvas

Controls
- [ ] Button
- [ ] Checkbox
- [ ] Slider
- [ ] TextField
- [ ] Dropdown
- [ ] TabButton

Data & Collections
- [ ] DataBoundList
- [ ] DataBoundGrid
- [ ] ItemSlot
- [ ] InteractiveSlot

Decorative & Visual
- [ ] Label
- [ ] Image
- [ ] Placeholder
- [ ] Separator
- [ ] ProgressBar
- [ ] ProcessIndicator

Advanced / Specialized
- [ ] PanCanvas
- [ ] DraggableBox
- [ ] AttributeBar
- [ ] EntityPaperdoll

Notes
- 若你想要更細粒度的測試標記（例如記錄測試人員、測試日期或測試備註），我可以把這個檔案擴展成一個表格或 YAML 格式，或把它轉為一個簡單的 JSON 檔由 CI/工具讀取。

