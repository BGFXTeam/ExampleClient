package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class SavePos extends Module {
    // enable = save current pos; disable = teleport back to it
    public SavePos() {
        super("SavePos", "Enable: saves pos. Disable: teleports back to saved pos", ModuleCategory.MOVEMENT);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_sp_saved=PlayerManager:getClientPlayer().Player:getPosition()\n" +
            "UIHelper.showToast('Position saved!')"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _sp_saved then\n" +
            "  PlayerManager:getClientPlayer().Player:setPosition(_sp_saved)\n" +
            "  UIHelper.showToast('Teleported back!')\n" +
            "end"
        );
    }
}