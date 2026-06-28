package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class FlyV2 extends Module {
    private final Setting.FloatSetting jumpCount;

    public FlyV2() {
        super("FlyV2", "Infinite double-jump flight", ModuleCategory.MOVEMENT);
        jumpCount = new Setting.FloatSetting("Jump Count", this, 1e10f, 1f, 1e10f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_fv2=LuaTimer:scheduleTimer(function()\n" +
            "  ClientHelper.putBoolPrefs('EnableDoubleJumps',true)\n" +
            "  PlayerManager:getClientPlayer().doubleJumpCount=10000000000\n" +
            "  ClientHelper.putBoolPrefs('IsViewBobbing',false)\n" +
            "  PlayerManager:getClientPlayer().Player.m_keepJumping=false\n" +
            "end,100,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _fv2 then LuaTimer:cancel(_fv2) _fv2=nil end\n" +
            "ClientHelper.putBoolPrefs('EnableDoubleJumps',false)\n" +
            "PlayerManager:getClientPlayer().doubleJumpCount=2"
        );
    }
}