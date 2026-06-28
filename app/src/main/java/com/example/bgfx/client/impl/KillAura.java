package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class KillAura extends Module {
    private final Setting.FloatSetting   range;
    private final Setting.FloatSetting   delay;
    private final Setting.BooleanSetting teamCheck;

    public KillAura() {
        super("KillAura", "Auto-attacks nearby enemy players", ModuleCategory.COMBAT);
        range     = new Setting.FloatSetting  ("Range",     this, 5f, 1f, 20f);
        delay     = new Setting.FloatSetting  ("Delay ms",  this, 50f, 10f, 500f);
        teamCheck = new Setting.BooleanSetting("TeamCheck", this, true);
    }

    public void onEnable() {
        float r2   = range.getValue() * range.getValue();
        int   dlay = (int) delay.getValue();
        ScriptManager.execute(
            "_ka_r2=" + r2 + " _ka_tc=" + teamCheck.getValue() + "\n" +
            "_ka=LuaTimer:scheduleTimer(function()\n" +
            "  local me=PlayerManager:getClientPlayer() if not me then return end\n" +
            "  local myTeam=me.Player:getTeamId()\n" +
            "  local myPos=me.Player:getPosition()\n" +
            "  for _,p in pairs(PlayerManager:getPlayers()) do\n" +
            "    if p~=me and p.Player then\n" +
            "      if not (_ka_tc and p.Player:getTeamId()==myTeam) then\n" +
            "        local d=MathUtil:distanceSquare3d(p:getPosition(),myPos)\n" +
            "        if d<_ka_r2 then\n" +
            "          CGame.Instance():attackEntity(p.Player:getEntityId())\n" +
            "        end\n" +
            "      end\n" +
            "    end\n" +
            "  end\n" +
            "end," + dlay + ",-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _ka then LuaTimer:cancel(_ka) _ka=nil end");
    }
}