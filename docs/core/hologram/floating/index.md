# Floating / VFX System

The Floating system handles floating objects and visual-effect related data and scheduling.

## 類別架構關係
- `FloatingCore` 是浮動物件 / VFX 的核心 API 入口。
- Floating / VFX 類別通常會和 `GameInstance`、事件系統與 client render 流程銜接。
- 這一層承接短生命週期、動態顯示、飄字與特效調度。

## 目前進度
- ✅ Floating / VFX 文件已整理到 canonical `docs/floating/`。
- ✅ 舊版粒子 / 懸浮物體總覽已收斂成 legacy 入口。
- ⏳ 如果未來要把實際 runtime 類別列成完整 public class 清單，會再補 docs。

## Public class navigation list
- [FloatingCore](FloatingCore.md)

## Large demo
```java
FloatingSystem system = new FloatingSystem();
system.spawnText("+10", new Vector3f(0, 64, 0));
system.tick(1.0f);
```

## Reading order
1. `FloatingCore.md`
