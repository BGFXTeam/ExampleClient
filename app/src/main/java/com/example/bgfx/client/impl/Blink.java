package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Blink extends Module {
    private final Setting.FloatSetting duration; // auto-sync after N seconds, 0 = manual

    public Blink() {
        super("Blink", "Desyncs client position from server", ModuleCategory.MOVEMENT);
        duration = new Setting.FloatSetting("Auto-sync sec", this, 0f, 0f, 30f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_bk=LuaTimer:scheduleTimer(function()\n" +
            "  ClientHelper.putBoolPrefs('SyncClientPositionToServer',false)\n" +
            "end,100,-1)"
        );
        float dur = duration.getValue();
        if (dur > 0) {
            ScriptManager.execute(
                "LuaTimer:scheduleTimer(function()\n" +
                "  ClientHelper.putBoolPrefs('SyncClientPositionToServer',true)\n" +
                "  if _bk then LuaTimer:cancel(_bk) _bk=nil end\n" +
                "end,1," + (int)(dur * 1000) + ")"
            );
        }
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _bk then LuaTimer:cancel(_bk) _bk=nil end\n" +
            "ClientHelper.putBoolPrefs('SyncClientPositionToServer',true)"
        );
    }
}