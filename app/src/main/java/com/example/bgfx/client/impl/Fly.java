package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Fly extends Module {
    private final Setting.FloatSetting speed;
    private final Setting.FloatSetting liftY;

    public Fly() {
        super("Fly", "Dev fly using setAllowFlying + moveEntity", ModuleCategory.MOVEMENT);
        speed = new Setting.FloatSetting("Speed",  this, 10000f, 1000f, 50000f);
        liftY = new Setting.FloatSetting("Lift Y", this, 1.35f,  0.5f,  5f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "local p=PlayerManager:getClientPlayer().Player\n" +
            "p:setSpeedAdditionLevel(0)\n" +
            "p:setAllowFlying(false)\n" +
            "p:setFlying(false)\n" +
            "local mv=VectorUtil.newVector3(0," + liftY.getValue() + ",0)\n" +
            "p:setAllowFlying(true)\n" +
            "p:setSpeedAdditionLevel(" + (int)speed.getValue() + ")\n" +
            "p:setFlying(true)\n" +
            "p:moveEntity(mv)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "local p=PlayerManager:getClientPlayer().Player\n" +
            "p:setSpeedAdditionLevel(0)\n" +
            "p:setAllowFlying(false)\n" +
            "p:setFlying(false)"
        );
    }
}