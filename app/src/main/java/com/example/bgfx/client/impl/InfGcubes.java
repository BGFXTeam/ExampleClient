package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class InfGcubes extends Module {
    private final Setting.FloatSetting amount;

    public InfGcubes() {
        super("InfGcubes", "Sets Gcube wallet to large value client-side", ModuleCategory.MISC);
        amount = new Setting.FloatSetting("Amount", this, 99999999f, 1000f, 1e9f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "local p=Game:getPlayer()\n" +
            "if p then p:getWallet().m_diamondGolds=" + (long)amount.getValue() + " end\n" +
            "ClientHelper.putIntPrefs('LocalGcube'," + (int)amount.getValue() + ")"
        );
    }

    public void onDisable() { /* client-side only */ }
}