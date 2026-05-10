# Slider

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

