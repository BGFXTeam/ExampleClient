package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class GetPoints extends Module {
    private final Setting.FloatSetting amount;

    public GetPoints() {
        super("GetPoints", "Sends point grant packet to server", ModuleCategory.MISC);
        amount = new Setting.FloatSetting("Amount", this, 1e11f, 1f, 1e12f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "MsgSender.sendOtherTips(" + (long)amount.getValue() + ",'&$[ffca00ff-fbd33fff]$$&')"
        );
    }

    public void onDisable() { /* one-shot */ }
}