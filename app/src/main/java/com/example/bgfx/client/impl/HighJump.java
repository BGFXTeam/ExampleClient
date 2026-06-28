package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class HighJump extends Module {
    private final Setting.FloatSetting multiplier;

    public HighJump() {
        super("HighJump", "Increases jump height via velocity override", ModuleCategory.MOVEMENT);
        multiplier = new Setting.FloatSetting("Multiplier", this, 3f, 1f, 20f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_hj_mult=" + multiplier.getValue() + " _hj_wasGround=true\n" +
            "_hj=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer().Player\n" +
            "  local onGround=(p.fallDistance==0)\n" +
            "  if _hj_wasGround and not onGround then\n" +
            "    -- just left ground: boost Y velocity\n" +
            "    local pos=p:getPosition()\n" +
            "    local cur=p:getVelocity and p:getVelocity() or VectorUtil.newVector3(0,0,0)\n" +
            "    p:setVelocity(VectorUtil.newVector3(cur.x,_hj_mult*0.42,cur.z))\n" +
            "  end\n" +
            "  _hj_wasGround=onGround\n" +
            "end,50,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _hj then LuaTimer:cancel(_hj) _hj=nil end");
    }
}