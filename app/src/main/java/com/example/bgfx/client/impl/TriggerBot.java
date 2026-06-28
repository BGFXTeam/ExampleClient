package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class TriggerBot extends Module {
    private final Setting.FloatSetting   range;
    private final Setting.FloatSetting   delay;
    private final Setting.BooleanSetting teamCheck;

    public TriggerBot() {
        super("TriggerBot", "Auto-clicks only when aiming at an enemy", ModuleCategory.COMBAT);
        range     = new Setting.FloatSetting  ("Range",     this, 4f,  1f, 15f);
        delay     = new Setting.FloatSetting  ("Delay ms",  this, 40f, 10f, 300f);
        teamCheck = new Setting.BooleanSetting("TeamCheck", this, true);
    }

    public void onEnable() {
        float r2 = range.getValue() * range.getValue();
        ScriptManager.execute(
            "_tb_r2=" + r2 + " _tb_tc=" + teamCheck.getValue() + "\n" +
            "_tb=LuaTimer:scheduleTimer(function()\n" +
            "  local me=PlayerManager:getClientPlayer() if not me then return end\n" +
            "  local myPos=me.Player:getPosition()\n" +
            "  local myYaw=me.Player.rotationYaw\n" +
            "  local myTeam=me.Player:getTeamId()\n" +
            "  for _,p in pairs(PlayerManager:getPlayers()) do\n" +
            "    if p~=me and p.Player then\n" +
            "      if not (_tb_tc and p.Player:getTeamId()==myTeam) then\n" +
            "        local d=MathUtil:distanceSquare3d(p:getPosition(),myPos)\n" +
            "        if d<_tb_r2 then\n" +
            "          CGame.Instance():handleTouchClick(650,400)\n" +
            "          return\n" +
            "        end\n" +
            "      end\n" +
            "    end\n" +
            "  end\n" +
            "end," + (int)delay.getValue() + ",-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _tb then LuaTimer:cancel(_tb) _tb=nil end");
    }
}