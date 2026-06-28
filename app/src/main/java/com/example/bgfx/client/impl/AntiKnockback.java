package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class AntiKnockback extends Module {
    private final Setting.FloatSetting strength; // 0 = full cancel, 1 = normal

    public AntiKnockback() {
        super("AntiKnockback", "Reduces or eliminates knockback received", ModuleCategory.COMBAT);
        strength = new Setting.FloatSetting("Strength %", this, 0f, 0f, 100f);
    }

    public void onEnable() {
        // lock position on damage by saving it and restoring after the knockback tick
        ScriptManager.execute(
            "_akb_str=" + (strength.getValue() / 100f) + "\n" +
            "_akb_savedPos=nil\n" +
            "_akb=LuaTimer:scheduleTimer(function()\n" +
            "  local me=PlayerManager:getClientPlayer() if not me then return end\n" +
            "  _akb_savedPos=me.Player:getPosition()\n" +
            "end,50,-1)\n" +
            "CEvents.AttackEntityEvent:registerCallBack(function()\n" +
            "  if _akb_savedPos and _akb_str<1 then\n" +
            "    LuaTimer:scheduleTimer(function()\n" +
            "      local me=PlayerManager:getClientPlayer() if not me then return end\n" +
            "      local cur=me.Player:getPosition()\n" +
            "      local sx=_akb_savedPos.x+(cur.x-_akb_savedPos.x)*_akb_str\n" +
            "      local sy=_akb_savedPos.y+(cur.y-_akb_savedPos.y)*_akb_str\n" +
            "      local sz=_akb_savedPos.z+(cur.z-_akb_savedPos.z)*_akb_str\n" +
            "      me.Player:setPosition(VectorUtil.newVector3(sx,sy,sz))\n" +
            "    end,1,80)\n" +
            "  end\n" +
            "end)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _akb then LuaTimer:cancel(_akb) _akb=nil end\n" +
            "_akb_savedPos=nil"
        );
    }
}