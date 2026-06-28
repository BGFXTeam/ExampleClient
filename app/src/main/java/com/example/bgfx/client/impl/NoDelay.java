package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class NoDelay extends Module {
    public NoDelay() {
        super("NoDelay", "Removes attack cooldown (0CD click)", ModuleCategory.COMBAT);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_nd=LuaTimer:scheduleTimer(function()\n" +
            "  ClientHelper.putBoolPrefs('banClickCD',true)\n" +
            "  ClientHelper.putBoolPrefs('0CDClick',true)\n" +
            "  ClientHelper.putBoolPrefs('RemoveClickCD',true)\n" +
            "  ClientHelper.putBoolPrefs('RemoveCDClick',true)\n" +
            "  ClientHelper.putBoolPrefs('0CDDelayClick',true)\n" +
            "  ClientHelper.putBoolPrefs('Bedward0CDClick',true)\n" +
            "end,100,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _nd then LuaTimer:cancel(_nd) _nd=nil end\n" +
            "ClientHelper.putBoolPrefs('banClickCD',false)\n" +
            "ClientHelper.putBoolPrefs('0CDClick',false)\n" +
            "ClientHelper.putBoolPrefs('RemoveClickCD',false)\n" +
            "ClientHelper.putBoolPrefs('RemoveCDClick',false)\n" +
            "ClientHelper.putBoolPrefs('0CDDelayClick',false)\n" +
            "ClientHelper.putBoolPrefs('Bedward0CDClick',false)"
        );
    }
}