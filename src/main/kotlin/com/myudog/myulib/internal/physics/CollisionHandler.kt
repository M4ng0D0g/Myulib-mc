package com.myudog.myulib.internal.physics

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

internal object CollisionHandler {
    /**
     * 處理粒子與世界的碰撞
     * @return 修正後的 (位置, 速度)
     */
    fun handle(world: ServerWorld, currentWorldPos: Vec3d, velocity: Vec3d, bounce: Double = 0.6): Pair<Vec3d, Vec3d> {
        val nextPos = currentWorldPos.add(velocity)
        val blockPos = BlockPos.ofFloored(nextPos.x, nextPos.y, nextPos.z)
        val state = world.getBlockState(blockPos)

        // 如果下一個位置是實心方塊
        if (state.isFullCube(world, blockPos)) {
            // 簡化模型：偵測碰撞面法向量 (這裡以最鄰近面為準)
            // 實戰中建議使用 world.raycast 進行精確判定
            val normal = getSimpleNormal(currentWorldPos, blockPos)

            // 反射公式：V_new = V_old - 2 * (V_old ⋅ N) * N
            val dot = velocity.dotProduct(normal)
            val reflection = velocity.subtract(normal.multiply(2.0 * dot)).multiply(bounce)

            return currentWorldPos to reflection // 停在原地並反彈
        }

        return nextPos to velocity
    }

    private fun getSimpleNormal(pos: Vec3d, block: BlockPos): Vec3d {
        // 找出粒子最靠近方塊的哪一面
        val dx = pos.x - (block.x + 0.5)
        val dy = pos.y - (block.y + 0.5)
        val dz = pos.z - (block.z + 0.5)
        return when {
            Math.abs(dy) > Math.abs(dx) && Math.abs(dy) > Math.abs(dz) -> Vec3d(0.0, Math.signum(dy), 0.0)
            Math.abs(dx) > Math.abs(dz) -> Vec3d(Math.signum(dx), 0.0, 0.0)
            else -> Vec3d(0.0, 0.0, Math.signum(dz))
        }
    }
}