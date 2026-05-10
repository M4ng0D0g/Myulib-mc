# Slider
## Role
This page is the canonical reference for `Slider` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Slider

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Slider.java`

Description

A slider widget supporting horizontal or vertical orientation. Internally uses `SliderComponent` for state (value, dragging, percentage). Visuals are drawn manually in `draw`.

Public API

- Constructor:
  - `Slider(label: String? = null, showValue: Boolean = true)`
- Properties:
  - `var isVertical: Boolean`
  - `var trackTextureKey: String?`
  - `var thumbTextureKey: String?`
- Fluent methods:
  - `fun withRange(min: Double, max: Double): Slider`
  - `fun withValue(v: Double): Slider`
  - `fun withStep(s: Double): Slider`
  - `fun onValueChanged(handler: (Double) -> Unit): Slider`

Usage example

```java
val s = Slider("Volume").withRange(0.0, 1.0).withValue(0.5).onValueChanged { v -> println(v) }
```

