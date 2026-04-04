package com.myudog.myulib.client.api.command

import com.mojang.brigadier.CommandDispatcher
import com.myudog.myulib.api.MyuVFX
import com.myudog.myulib.api.MyuVFXManager
import com.myudog.myulib.client.gui.test.DemoUIScreen
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.text.Text
import org.joml.Vector3f

object TestCommandsRegistry {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            dispatcher.register(
                literal("myulib").then(
                    literal("test")
                        .then(
                            literal("ui").executes { context ->
                                val client = MinecraftClient.getInstance()
                                client.send {
                                    client.setScreen(DemoUIScreen())
                                }
                                context.source.sendFeedback(Text.literal("Opening UI Test Screen"))
                                1
                            }
                        )
                        .then(
                            literal("vfx").executes { context ->
                                val player = context.source.player
                                val server = MinecraftClient.getInstance().server
                                val serverWorld = server?.getWorld(player.entityWorld?.registryKey ?: net.minecraft.world.World.OVERWORLD) ?: server?.worlds?.firstOrNull()
                                
                                if (serverWorld != null) {
                                    val ePos = player.entityPos
                                    MyuVFXManager.spawnSpiral(serverWorld, ePos, ParticleTypes.END_ROD)
                                    MyuVFXManager.spawnShockwave(serverWorld, ePos.add(2.0, 0.0, 2.0), ParticleTypes.FLAME)
                                    context.source.sendFeedback(Text.literal("Spawning test VFX..."))
                                } else {
                                    context.source.sendError(Text.literal("Could not find ServerWorld. Are you in singleplayer?"))
                                }
                                1
                            }
                        )
                        .then(
                            literal("floatobj").executes { context ->
                                val player = context.source.player
                                val server = MinecraftClient.getInstance().server
                                val serverWorld = server?.getWorld(player.entityWorld?.registryKey ?: net.minecraft.world.World.OVERWORLD) ?: server?.worlds?.firstOrNull()
                                
                                if (serverWorld != null) {
                                    val itemObj = MyuVFX.createItemObject(serverWorld, Items.DIAMOND_SWORD.defaultStack)
                                    val spawnPos = player.entityPos.add(0.0, 2.0, 0.0)
                                    itemObj.spawn(spawnPos)
                                    
                                    // Make it do a small jump and spin
                                    itemObj.moveTo(spawnPos.add(0.0, 1.0, 0.0), 20)
                                    itemObj.setRotation(Vector3f(0f, 3.14f, 0f), 20)
                                    
                                    context.source.sendFeedback(Text.literal("Spawning test FloatingObject..."))
                                } else {
                                    context.source.sendError(Text.literal("Could not find ServerWorld. Are you in singleplayer?"))
                                }
                                1
                            }
                        )
                )
            )
        }
    }
}
