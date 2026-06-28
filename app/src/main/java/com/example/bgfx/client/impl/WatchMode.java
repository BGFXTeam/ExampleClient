package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class WatchMode extends Module {
    public WatchMode() {
        super("WatchMode", "Activates spectator/watch mode fly", ModuleCategory.MISC);
    }

    public void onEnable() {
        ScriptManager.execute(
            "local p=PlayerManager:getClientPlayer().Player\n" +
            "local mv=VectorUtil.newVector3(0,1.35,0)\n" +
            "p:setAllowFlying(true)\n" +
            "p:setFlying(true)\n" +
            "p:setWatchMode(true)\n" +
            "p:moveEntity(mv)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "local p=PlayerManager:getClientPlayer().Player\n" +
            "p:setAllowFlying(false)\n" +
            "p:setFlying(false)\n" +
            "p:setWatchMode(false)"
        );
    }
}