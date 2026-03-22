package com.myudog.myulib

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Myulib : ModInitializer {

    val LOGGER: Logger = LoggerFactory.getLogger("myulib")



    override fun onInitialize() {
        LOGGER.info("Mango UI is initializing...")



        LOGGER.info("MyuLib (by MyuDog) has been initialized successfully.")
    }
}