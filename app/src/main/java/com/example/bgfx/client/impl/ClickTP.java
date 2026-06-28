package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class ClickTP extends Module {
    private final Setting.FloatSetting offsetY;

    public ClickTP() {
        super("ClickTP", "Teleport to any clicked block", ModuleCategory.MOVEMENT);
        offsetY = new Setting.FloatSetting("Y Offset", this, 3f, 0f, 10f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_ctp_oy=" + offsetY.getValue() + "\n" +
            "ClientHelper.putFloatPrefs('BlockReachDistance',9999)\n" +
            "_ctp_cb=function(pos)\n" +
            "  local me=PlayerManager:getClientPlayer()\n" +
            "  if me then\n" +
            "    me.Player:setPosition(VectorUtil.newVector3(pos.x+0.7,pos.y+_ctp_oy,pos.z+0.7))\n" +
            "  end\n" +
            "end\n" +
            "Listener.registerCallBack(CEvents.ClickToBlockEvent,_ctp_cb)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "ClientHelper.putFloatPrefs('BlockReachDistance',6.5)\n" +
            "_ctp_cb=nil"
        );
    }
}