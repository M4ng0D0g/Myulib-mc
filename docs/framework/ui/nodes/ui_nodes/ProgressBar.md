# ProgressBar

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/ProgressBar.java`

Description

A configurable progress bar supporting ghost progress (e.g., health drop shadow), text label, and configurable lerp speed and direction.

Public API

- Constructor:
  - `ProgressBar()`
- Fluent methods / configuration:
  - `fun withProgress(p: Float): ProgressBar`
  - `fun withSupplier(s: () -> Float): ProgressBar`
  - `fun withLerp(speed: Float): ProgressBar`
  - `fun withDirection(d: ProgressDirection): ProgressBar`
  - `fun showPercentage(): ProgressBar`

Usage example

```java
val pb = ProgressBar().withProgress(0.5f).showPercentage()
```

