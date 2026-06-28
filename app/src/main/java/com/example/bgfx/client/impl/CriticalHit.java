package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class CriticalHit extends Module {
    private final Setting.FloatSetting drop;

    public CriticalHit() {
        super("CriticalHit", "Micro-drops position to trigger critical hits", ModuleCategory.COMBAT);
        drop = new Setting.FloatSetting("Drop", this, 0.1f, 0.01f, 0.5f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_ch_drop=" + drop.getValue() + "\n" +
            "_ch=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer() if not p then return end\n" +
            "  local pos=p.Player:getPosition()\n" +
            "  p.Player:setPosition(VectorUtil.newVector3(pos.x,pos.y-_ch_drop,pos.z))\n" +
            "end,80,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _ch then LuaTimer:cancel(_ch) _ch=nil end");
    }
}