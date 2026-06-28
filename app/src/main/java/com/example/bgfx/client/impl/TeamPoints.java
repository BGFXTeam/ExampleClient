package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class TeamPoints extends Module {
    private final Setting.FloatSetting packets;

    public TeamPoints() {
        super("TeamPoints", "Sends repeated team scoring packets", ModuleCategory.MISC);
        packets = new Setting.FloatSetting("Count", this, 10f, 1f, 100f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "local me=PlayerManager:getClientPlayer()\n" +
            "if not me then return end\n" +
            "local tid=me:getTeamId()\n" +
            "local cnt=0\n" +
            "_tp_t=LuaTimer:scheduleTimer(function()\n" +
            "  MsgSender.sendOtherTips(100000000000,'&$[ffca00ff-fbd33fff]$$&')\n" +
            "  cnt=cnt+1\n" +
            "  if cnt>=" + (int)packets.getValue() + " then\n" +
            "    LuaTimer:cancel(_tp_t) _tp_t=nil\n" +
            "  end\n" +
            "end,500," + (int)packets.getValue() + ")"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _tp_t then LuaTimer:cancel(_tp_t) _tp_t=nil end");
    }
}