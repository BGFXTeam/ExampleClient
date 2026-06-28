package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class AutoClicker extends Module {
    private final Setting.FloatSetting   cps;
    private final Setting.FloatSetting   clickX;
    private final Setting.FloatSetting   clickY;
    private final Setting.BooleanSetting onlyEnemy; // only when enemy in range

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks at target CPS", ModuleCategory.COMBAT);
        cps       = new Setting.FloatSetting  ("CPS",       this, 20f, 1f, 60f);
        clickX    = new Setting.FloatSetting  ("Click X",   this, 1204f, 0f, 1280f);
        clickY    = new Setting.FloatSetting  ("Click Y",   this, 540f,  0f, 720f);
        onlyEnemy = new Setting.BooleanSetting("OnlyEnemy", this, false);
    }

    public void onEnable() {
        int interval = (int)(1000f / cps.getValue());
        ScriptManager.execute(
            "_ac_cx=" + (int)clickX.getValue() + " _ac_cy=" + (int)clickY.getValue() + "\n" +
            "_ac_oe=" + onlyEnemy.getValue() + "\n" +
            "_ac=LuaTimer:scheduleTimer(function()\n" +
            "  if _ac_oe then\n" +
            "    local me=PlayerManager:getClientPlayer() if not me then return end\n" +
            "    local myPos=me.Player:getPosition()\n" +
            "    local found=false\n" +
            "    for _,p in pairs(PlayerManager:getPlayers()) do\n" +
            "      if p~=me and MathUtil:distanceSquare3d(p:getPosition(),myPos)<25 then found=true break end\n" +
            "    end\n" +
            "    if not found then return end\n" +
            "  end\n" +
            "  CGame.Instance():handleTouchClick(_ac_cx,_ac_cy)\n" +
            "end," + interval + ",-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _ac then LuaTimer:cancel(_ac) _ac=nil end");
    }
}