package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class JetPack extends Module {
    private final Setting.FloatSetting power;

    public JetPack() {
        super("JetPack", "Directional velocity-based flight (yaw/pitch)", ModuleCategory.MOVEMENT);
        power = new Setting.FloatSetting("Power", this, 3f, 0.5f, 20f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_jp_pw=" + power.getValue() + "\n" +
            "_jp=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer().Player\n" +
            "  local yaw=p:getYaw() local pitch=p:getPitch()\n" +
            "  local yawR=math.rad(yaw) local pitchR=math.rad(pitch)\n" +
            "  local vx=-_jp_pw*math.cos(pitchR)*math.sin(yawR)\n" +
            "  local vy=-_jp_pw*math.sin(pitchR)\n" +
            "  local vz= _jp_pw*math.cos(pitchR)*math.cos(yawR)\n" +
            "  p:setVelocity(VectorUtil.newVector3(vx,vy,vz))\n" +
            "end,5,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _jp then LuaTimer:cancel(_jp) _jp=nil end\n" +
            "PlayerManager:getClientPlayer().Player:setVelocity(VectorUtil.newVector3(0,0,0))"
        );
    }
}