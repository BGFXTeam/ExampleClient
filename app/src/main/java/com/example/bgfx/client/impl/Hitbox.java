package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Hitbox extends Module {
    private final Setting.FloatSetting width;
    private final Setting.FloatSetting height;
    private final Setting.FloatSetting length;

    public Hitbox() {
        super("Hitbox", "Expands enemy hitboxes for easier hits", ModuleCategory.COMBAT);
        width  = new Setting.FloatSetting("Width",  this, 2f,  0.6f, 10f);
        height = new Setting.FloatSetting("Height", this, 2.3f, 1.8f, 10f);
        length = new Setting.FloatSetting("Length", this, 2f,  0.6f, 10f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_hb_w=" + width.getValue() + " _hb_h=" + height.getValue() + " _hb_l=" + length.getValue() + "\n" +
            "local me=PlayerManager:getClientPlayer()\n" +
            "for _,p in pairs(PlayerManager:getPlayers()) do\n" +
            "  if p~=me and p.Player then\n" +
            "    p.Player.height=_hb_h p.Player.width=_hb_w p.Player.length=_hb_l\n" +
            "  end\n" +
            "end\n" +
            // timer to keep expanding newly joined players
            "_hb=LuaTimer:scheduleTimer(function()\n" +
            "  local me2=PlayerManager:getClientPlayer()\n" +
            "  for _,p in pairs(PlayerManager:getPlayers()) do\n" +
            "    if p~=me2 and p.Player then\n" +
            "      if p.Player.height~=_hb_h then\n" +
            "        p.Player.height=_hb_h p.Player.width=_hb_w p.Player.length=_hb_l\n" +
            "      end\n" +
            "    end\n" +
            "  end\n" +
            "end,2000,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _hb then LuaTimer:cancel(_hb) _hb=nil end\n" +
            "local me=PlayerManager:getClientPlayer()\n" +
            "for _,p in pairs(PlayerManager:getPlayers()) do\n" +
            "  if p~=me and p.Player then\n" +
            "    p.Player.height=1.8 p.Player.width=0.6 p.Player.length=0.6\n" +
            "  end\n" +
            "end"
        );
    }
}