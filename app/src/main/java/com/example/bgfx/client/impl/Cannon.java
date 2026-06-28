package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Cannon extends Module {
    private final Setting.FloatSetting power;
    private final Setting.BooleanSetting parachute;

    public Cannon() {
        super("Cannon", "Launches player in look direction (one-shot)", ModuleCategory.MISC);
        power     = new Setting.FloatSetting  ("Power",     this, 3f,   1f, 20f);
        parachute = new Setting.BooleanSetting("Parachute", this, true);
    }

    public void onEnable() {
        ScriptManager.execute(
            "local p=PlayerManager:getClientPlayer()\n" +
            "if p then\n" +
            "  local pitch=p.Player:getPitch() local yaw=p.Player:getYaw()\n" +
            "  local pr=pitch*math.pi/180 local yr=yaw*-math.pi/180\n" +
            "  local pw=" + power.getValue() + "\n" +
            "  local vx=pw*math.cos(pr)*math.sin(yr)\n" +
            "  local vy=-pw*math.sin(pr)\n" +
            "  local vz=pw*math.cos(pr)*math.cos(yr)\n" +
            "  p.Player:setVelocity(VectorUtil.newVector3(vx,vy,vz))\n" +
            (parachute.getValue() ? "  p.Player:startParachute()\n" : "") +
            "end"
        );
    }

    public void onDisable() { /* one-shot */ }
}