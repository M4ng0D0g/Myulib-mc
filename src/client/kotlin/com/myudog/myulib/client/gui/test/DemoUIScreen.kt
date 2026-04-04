package com.myudog.myulib.client.gui.test

import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.screen.EcsScreen
import com.myudog.myulib.client.api.ui.theme.DefaultDarkTheme
import com.myudog.myulib.client.api.ui.theme.DefaultLightTheme
import com.myudog.myulib.client.api.ui.theme.ThemeManager
import com.myudog.myulib.client.api.ui.theme.UiTheme
import com.myudog.myulib.client.api.ui.node.Button
import com.myudog.myulib.client.api.ui.node.Label
import com.myudog.myulib.client.api.ui.node.Checkbox
import com.myudog.myulib.client.api.ui.node.Slider
import net.minecraft.text.Text

class DemoUIScreen : EcsScreen(Text.literal("MyuLib UI Sandbox"), {
    val theme = ThemeManager.current

    // Setup root container (this refers to the root Box in builder)
    this.transform.width = SizeUnit.Fixed(400f)
    this.transform.height = SizeUnit.Fixed(300f)
    this.transform.offsetX = 50f
    this.transform.offsetY = 50f

    val titleLabel = Label(Text.literal("MyuLib UI Sandbox"))
    titleLabel.transform.offsetX = 10f
    titleLabel.transform.offsetY = 10f
    titleLabel.color = theme.textPrimary.argb
    this.addChild(titleLabel)

    val toggleBtn = Button {
        val newTheme = if (ThemeManager.current.name == "Default Dark") DefaultLightTheme else DefaultDarkTheme
        ThemeManager.setTheme(newTheme)
    }
    toggleBtn.label = "Toggle Theme"
    toggleBtn.transform.offsetX = 10f
    toggleBtn.transform.offsetY = 40f
    toggleBtn.transform.width = SizeUnit.Fixed(100f)
    toggleBtn.transform.height = SizeUnit.Fixed(20f)
    this.addChild(toggleBtn)

    val cb = Checkbox("Test Checkbox", true)
    cb.transform.offsetX = 10f
    cb.transform.offsetY = 70f
    cb.transform.width = SizeUnit.Fixed(120f)
    cb.transform.height = SizeUnit.Fixed(20f)
    this.addChild(cb)

    val testSlider = Slider("Test Slider", true).withValue(0.5)
    testSlider.transform.offsetX = 10f
    testSlider.transform.offsetY = 100f
    testSlider.transform.width = SizeUnit.Fixed(100f)
    testSlider.transform.height = SizeUnit.Fixed(20f)
    this.addChild(testSlider)
}) {
    
    override fun init() {
        super.init()
        ThemeManager.onThemeChangedListeners.add(::rebuildUi)
    }

    override fun removed() {
        super.removed()
        ThemeManager.onThemeChangedListeners.remove(::rebuildUi)
    }
    
    private fun rebuildUi() {
        // Here we ideally want to recreate the screen or refresh nodes. 
        // For simplicity in testing, we might just re-open the screen or re-init the root.
        // It's a sandbox demo.
    }
}

