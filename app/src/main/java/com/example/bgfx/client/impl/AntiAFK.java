package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class AntiAFK extends Module {
    private final Setting.FloatSetting interval;

    public AntiAFK() {
        super("AntiAFK", "Sends periodic clicks to prevent AFK kick", ModuleCategory.MISC);
        interval = new Setting.FloatSetting("Interval ms", this, 4000f, 500f, 30000f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_afk=LuaTimer:scheduleTimer(function()\n" +
            "  CGame.Instance():handleTouchClick(1204,540)\n" +
            "end," + (int)interval.getValue() + ",-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _afk then LuaTimer:cancel(_afk) _afk=nil end");
    }
}