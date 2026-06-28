package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class GodMode extends Module {
    private final Setting.FloatSetting maxHp;

    public GodMode() {
        super("GodMode", "Continuously restores HP to max", ModuleCategory.MISC);
        maxHp = new Setting.FloatSetting("Max HP", this, 20f, 1f, 100f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_gm_hp=" + (int)maxHp.getValue() + "\n" +
            "_gm=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer()\n" +
            "  if p and p.Player:getHealth()<_gm_hp then\n" +
            "    p.Player:setHealth(_gm_hp)\n" +
            "  end\n" +
            "end,200,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _gm then LuaTimer:cancel(_gm) _gm=nil end");
    }
}