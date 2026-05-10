# ProgressBar
## Role
This page is the canonical reference for `ProgressBar` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
ProgressBar

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

