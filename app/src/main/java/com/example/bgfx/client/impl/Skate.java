package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Skate extends Module {
    public Skate() {
        super("Skate", "Disables walk animation for skating effect", ModuleCategory.MOVEMENT);
    }

    public void onEnable() {
        ScriptManager.execute(
            "PlayerManager:getClientPlayer().Player:setBoolProperty('DisableUpdateAnimState',true)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "PlayerManager:getClientPlayer().Player:setBoolProperty('DisableUpdateAnimState',false)"
        );
    }
}