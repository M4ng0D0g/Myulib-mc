package com.myudog.myulib.api.dsl

import com.myudog.myulib.api.Shapes
import com.myudog.myulib.api.color.ColorProvider
import com.myudog.myulib.api.dynamics.IForceField
import com.myudog.myulib.api.`object`.IFloatingObject
import com.myudog.myulib.api.shape.IShape
import com.myudog.myulib.internal.math.VectorRotation
import com.myudog.myulib.internal.scheduler.EffectTicker
import com.myudog.myulib.internal.monitor.VFXMonitor
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

class EffectBuilder(center: Vec3d) {
    private var duration: Int = 20
    private val tickActions = mutableListOf<(Int) -> Unit>()
    private var centerProvider: () -> Vec3d = { center }

    fun duration(ticks: Int) { this.duration = ticks }

    /**
     * 物件跟隨設定
     */
    fun follow(entity: Entity, offset: Vec3d = Vec3d.ZERO) {
        this.centerProvider = { entity.entityPos.add(offset) }
    }

    fun follow(provider: () -> Vec3d) {
        this.centerProvider = provider
    }

    private fun getCurrentCenter(): Vec3d = centerProvider()

    /**
     * 註冊每幀執行的邏輯 (支援多個 Action)
     */
    fun onTick(action: (currentTick: Int) -> Unit) {
        this.tickActions.add(action)
    }

    fun bindObject(obj: IFloatingObject, path: (Int) -> Vec3d) {
        onTick { tick ->
            val nextPos = path(tick)
            obj.moveTo(nextPos, 1)
        }
    }

    /**
     * 終極渲染函數：整合 幾何、多重力場、碰撞、生命週期與動態色彩
     */
    fun renderShape(
        shape: IShape,
        isSolid: Boolean = false,
        size: Vec3d = Vec3d(1.0, 1.0, 1.0),
        density: Double = 2.0,
        // --- 色彩參數 (修正處：補齊 colorStart) ---
        colorStart: String = "#FFFFFF",
        colorEnd: String? = null,
        rainbowPeriod: Int = 0,
        // --- 物理與生命參數 ---
        forceFields: List<Pair<IForceField, Double>> = listOf(),
        friction: Double = 0.95,
        useCollision: Boolean = false,
        bounce: Double = 0.5,
        minLife: Int = 20,
        maxLife: Int = 40,
        // --- 渲染回調 ---
        onRender: (Vec3d, Vector3f, Float) -> Unit
    ) {
        // 1. 初始化點位與個體狀態
        val points = if (isSolid) shape.getSolidPoints(size, density) else shape.getOutlinePoints(size, density)
        val particles = points.map {
            val life = (minLife + (Math.random() * (maxLife - minLife))).toInt()
            com.myudog.myulib.internal.state.ParticleState(it, Vec3d.ZERO, 0, life)
        }.toMutableList()

        // 預先轉換顏色以提升效能
        val startRGB = ColorProvider.hexToRGB(colorStart)
        val endRGB = colorEnd?.let { ColorProvider.hexToRGB(it) }

        // 2. 註冊到 Tick 列表
        onTick { tick ->
            if (!VFXMonitor.requestSpawn(particles.size)) return@onTick

            val currentCenter = getCurrentCenter()
            // 這裡假設 EffectBuilder 能獲取 ServerWorld，通常從 spawnEffect 的傳入對象取得
            // 範例中使用廣域獲取或由外部傳入，這裡建議確保環境中有 world 實例
            val world = net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.START_SERVER_TICK // 僅示意

            val iterator = particles.iterator()
            while (iterator.hasNext()) {
                val p = iterator.next()
                p.age++

                // 檢查壽命
                if (p.isDead) {
                    iterator.remove()
                    continue
                }

                // A. 計算物理力場累加
                var totalForce = Vec3d.ZERO
                forceFields.forEach { (field, strength) ->
                    totalForce = totalForce.add(field.calculateForce(p.pos, Vec3d.ZERO, strength))
                }
                p.vel = p.vel.add(totalForce).multiply(friction)

                // B. 處理移動與碰撞
                if (useCollision) {
                    // 這邊需要獲取當前世界實例，建議在 EffectBuilder 初始化時傳入 world
                    // val (nextWorldPos, nextVel) = CollisionHandler.handle(world, currentCenter.add(p.pos), p.vel, bounce)
                    // p.pos = nextWorldPos.subtract(currentCenter)
                    // p.vel = nextVel
                    p.pos = p.pos.add(p.vel) // 暫時回退至單純移動，除非你已實作 world 傳遞
                } else {
                    p.pos = p.pos.add(p.vel)
                }

                // C. 計算當前色彩模式
                val currentColor = when {
                    rainbowPeriod > 0 -> {
                        // 加入隨機種子偏移，讓每個粒子色相略有不同
                        ColorProvider.getRainbowColor(tick + (p.randomSeed * 100).toInt(), rainbowPeriod)
                    }
                    endRGB != null -> {
                        // 隨著粒子生命進度 (0.0~1.0) 進行漸層
                        ColorProvider.lerp(startRGB, endRGB, p.progress)
                    }
                    else -> startRGB
                }

                // D. 執行渲染
                onRender(currentCenter.add(p.pos), currentColor, p.progress)
            }
        }
    }

    /**
     * 傳統圓形快捷方法 (同樣支援動態座標)
     */
    fun forCircle(radius: Double, density: Double, action: (Vec3d) -> Unit) {
        onTick {
            val centerPos = getCurrentCenter()
            Shapes.CIRCLE.getOutlinePoints(Vec3d(radius, 0.0, radius), density).forEach { offset ->
                action(centerPos.add(offset))
            }
        }
    }

    internal fun build() {
        var current = 0
        EffectTicker.addTask {
            if (current >= duration) return@addTask false
            // 執行所有註冊的任務
            tickActions.forEach { it.invoke(current) }
            current++
            true
        }
    }
}

fun spawnEffect(center: Vec3d, setup: EffectBuilder.() -> Unit) {
    EffectBuilder(center).apply(setup).build()
}