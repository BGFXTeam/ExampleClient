package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Aimbot extends Module {
    private final Setting.FloatSetting   range;
    private final Setting.BooleanSetting teamCheck;
    private final Setting.BooleanSetting autoClick;
    private final Setting.FloatSetting   clickX;
    private final Setting.FloatSetting   clickY;

    public Aimbot() {
        super("Aimbot", "Locks yaw/pitch onto nearest enemy", ModuleCategory.COMBAT);
        range     = new Setting.FloatSetting  ("Range",      this, 60f, 5f, 200f);
        teamCheck = new Setting.BooleanSetting("TeamCheck",  this, true);
        autoClick = new Setting.BooleanSetting("AutoClick",  this, true);
        clickX    = new Setting.FloatSetting  ("ClickX",     this, 650f, 0f, 1280f);
        clickY    = new Setting.FloatSetting  ("ClickY",     this, 400f, 0f, 720f);
    }

    public void onEnable() {
        float r2 = range.getValue() * range.getValue();
        ScriptManager.execute(
            "_abt_r2=" + r2 + " _abt_tc=" + teamCheck.getValue() +
            " _abt_ac=" + autoClick.getValue() +
            " _abt_cx=" + (int)clickX.getValue() + " _abt_cy=" + (int)clickY.getValue() + "\n" +
            "_abt=LuaTimer:scheduleTimer(function()\n" +
            "  local me=PlayerManager:getClientPlayer() if not me then return end\n" +
            "  local myTeam=me.Player:getTeamId()\n" +
            "  local cam=SceneManager.Instance():getMainCamera()\n" +
            "  local camPos=cam:getPosition()\n" +
            "  local best=nil local bestD=_abt_r2\n" +
            "  for _,p in pairs(PlayerManager:getPlayers()) do\n" +
            "    if p~=me and p.Player then\n" +
            "      if not (_abt_tc and p.Player:getTeamId()==myTeam) then\n" +
            "        local d=MathUtil:distanceSquare2d(p:getPosition(),me.Player:getPosition())\n" +
            "        if d<bestD then bestD=d best=p end\n" +
            "      end\n" +
            "    end\n" +
            "  end\n" +
            "  if best then\n" +
            "    local diff=VectorUtil.sub3(best:getPosition(),camPos)\n" +
            "    local yaw=(math.atan2(diff.x,diff.z)/math.pi)*-180\n" +
            "    local h2=math.sqrt(diff.x*diff.x+diff.z*diff.z)\n" +
            "    local pitch=(-math.atan2(diff.y+1.5,h2)/math.pi)*180\n" +
            "    me.Player.rotationYaw=yaw\n" +
            "    me.Player.rotationPitch=pitch\n" +
            "    if _abt_ac then CGame.Instance():handleTouchClick(_abt_cx,_abt_cy) end\n" +
            "  end\n" +
            "end,5,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _abt then LuaTimer:cancel(_abt) _abt=nil end");
    }
}