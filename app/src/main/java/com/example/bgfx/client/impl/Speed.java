package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Speed extends Module {
    private final Setting.FloatSetting level;

    public Speed() {
        super("Speed", "Increases movement speed via SpeedAdditionLevel", ModuleCategory.MOVEMENT);
        level = new Setting.FloatSetting("Level", this, 15000f, 1000f, 100000f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_sp_lv=" + (int)level.getValue() + "\n" +
            "_sp=LuaTimer:scheduleTimer(function()\n" +
            "  PlayerManager:getClientPlayer().Player:setSpeedAdditionLevel(_sp_lv)\n" +
            "end,100,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _sp then LuaTimer:cancel(_sp) _sp=nil end\n" +
            "PlayerManager:getClientPlayer().Player:setSpeedAdditionLevel(0)"
        );
    }
}