package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Fullbright extends Module {
    private final Setting.FloatSetting level;

    public Fullbright() {
        super("Fullbright", "Removes darkness / max visibility", ModuleCategory.VISUAL);
        level = new Setting.FloatSetting("Level", this, 10f, 1f, 20f);
    }

    public void onEnable() {
        ScriptManager.execute("ClientHelper.putFloatPrefs('Brightness'," + level.getValue() + ")");
    }

    public void onDisable() {
        ScriptManager.execute("ClientHelper.putFloatPrefs('Brightness',1.0)");
    }
}