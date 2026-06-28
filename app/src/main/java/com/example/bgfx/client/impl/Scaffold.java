package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Scaffold extends Module {
    private final Setting.FloatSetting blockId;
    private final Setting.FloatSetting delay;

    public Scaffold() {
        super("Scaffold", "Places blocks under your feet automatically", ModuleCategory.WORLD);
        blockId = new Setting.FloatSetting("Block ID", this, 1f, 1f, 255f);
        delay   = new Setting.FloatSetting("Delay ms", this, 100f, 50f, 500f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_sc_bid=" + (int)blockId.getValue() + "\n" +
            "_sc=LuaTimer:scheduleTimer(function()\n" +
            "  local pos=PlayerManager:getClientPlayer().Player:getPosition()\n" +
            "  local bp=VectorUtil.newVector3(math.floor(pos.x),math.floor(pos.y)-1,math.floor(pos.z))\n" +
            "  BlockManager:placeBlock(bp,_sc_bid)\n" +
            "end," + (int)delay.getValue() + ",-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _sc then LuaTimer:cancel(_sc) _sc=nil end");
    }
}