package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Parachute extends Module {
    public Parachute() {
        super("Parachute", "Activates player parachute", ModuleCategory.MISC);
    }

    public void onEnable() {
        ScriptManager.execute(
            "local p=PlayerManager:getClientPlayer()\n" +
            "if p then p.Player:startParachute() end"
        );
    }

    public void onDisable() { /* no stop API */ }
}