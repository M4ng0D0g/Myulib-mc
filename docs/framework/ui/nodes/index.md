# UI Nodes

This folder contains all UI node pages. Each page should describe the role, unique responsibility, concrete usage, fields, and methods.

## 類別架構關係
- `Box` / `Panel` / `ScrollBox` / `Grid` / `Stack` / `Canvas` 這些是容器節點。
- `Button` / `Checkbox` / `Slider` / `TextField` / `Dropdown` / `TabButton` / `TabGroup` 是互動節點。
- `Label` / `Image` / `Placeholder` / `Separator` / `ProgressBar` / `ProcessIndicator` 是視覺節點。
- `DataBoundList` / `DataBoundGrid` / `ItemSlot` / `InteractiveSlot` 是資料驅動 / 集合節點。
- `PanCanvas` / `DraggableBox` / `AttributeBar` / `EntityPaperdoll` 是進階節點。
- 這一層與 `LayoutSystem` / `RenderSystem` / `InputSystem` / `AnimationSystem` 互相銜接。

## 目前進度
- ✅ UI Nodes 已集中到 canonical `docs/ui/nodes/`。
- ✅ 已建立 node navigation list 與大示範入口。
- 🟡 各 node 頁的 fields / methods 正在逐步統一格式。
- ⏳ 後續將補齊每個 node 頁的「角色 / 唯一職責 / 實際寫法」說明。

## Navigation list
- [Box](Box.md)
- [Button](Button.md)
- [Canvas](Canvas.md)
- [Checkbox](Checkbox.md)
- [DataBoundGrid](DataBoundGrid.md)
- [DataBoundList](DataBoundList.md)
- [Dropdown](Dropdown.md)
- [DraggableBox](DraggableBox.md)
- [EntityPaperdoll](EntityPaperdoll.md)
- [Grid](Grid.md)
- [Image](Image.md)
- [InteractiveSlot](InteractiveSlot.md)
- [ItemSlot](ItemSlot.md)
- [Label](Label.md)
- [LayoutNodes](LayoutNodes.md)
- [PanCanvas](PanCanvas.md)
- [Panel](Panel.md)
- [Placeholder](Placeholder.md)
- [ProcessIndicator](ProcessIndicator.md)
- [ProgressBar](ProgressBar.md)
- [ScrollBox](ScrollBox.md)
- [Separator](Separator.md)
- [Slider](Slider.md)
- [Stack](Stack.md)
- [TabButton](TabButton.md)
- [TabGroup](TabGroup.md)
- [TextField](TextField.md)
- [Tooltip](Tooltip.md)

## Large demo
```java
// Typical one-file composition of a complex UI
// Box -> Header -> Content -> Footer -> Buttons
// then render it from a screen
```
