package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", "Prevents slowdown effects and keeps sprint active", ModuleCategory.MOVEMENT);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_ns=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer().Player\n" +
            "  p:setSprinting(true)\n" +
            "  ClientHelper.putIntPrefs('SprintLimitCheck',1)\n" +
            "end,200,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _ns then LuaTimer:cancel(_ns) _ns=nil end\n" +
            "ClientHelper.putIntPrefs('SprintLimitCheck',0)"
        );
    }
}