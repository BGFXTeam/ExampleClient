package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Reach extends Module {
    private final Setting.FloatSetting blockReach;
    private final Setting.FloatSetting entityReach;

    public Reach() {
        super("Reach", "Extends block and entity reach distance", ModuleCategory.WORLD);
        blockReach  = new Setting.FloatSetting("Block Reach",  this, 1000f, 5f, 9999f);
        entityReach = new Setting.FloatSetting("Entity Reach", this, 5f,    5f, 9999f);
    }

    public void onEnable() {
        // use timer so prefs persist across game resets
        ScriptManager.execute(
            "_rc_br=" + blockReach.getValue() + " _rc_er=" + entityReach.getValue() + "\n" +
            "_rc=LuaTimer:scheduleTimer(function()\n" +
            "  ClientHelper.putFloatPrefs('BlockReachDistance',_rc_br)\n" +
            "  ClientHelper.putFloatPrefs('EntityReachDistance',_rc_er)\n" +
            "end,100,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _rc then LuaTimer:cancel(_rc) _rc=nil end\n" +
            "ClientHelper.putFloatPrefs('BlockReachDistance',5)\n" +
            "ClientHelper.putFloatPrefs('EntityReachDistance',5)"
        );
    }
}