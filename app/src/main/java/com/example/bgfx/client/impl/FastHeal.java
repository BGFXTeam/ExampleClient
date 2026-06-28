package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class FastHeal extends Module {
    private final Setting.FloatSetting healPerTick;
    private final Setting.FloatSetting delay;
    private final Setting.FloatSetting maxHp;

    public FastHeal() {
        super("FastHeal", "Rapidly regenerates HP", ModuleCategory.MISC);
        healPerTick = new Setting.FloatSetting("Per Tick", this, 1f,    0.5f, 10f);
        delay       = new Setting.FloatSetting("Delay ms", this, 200f,  50f, 2000f);
        maxHp       = new Setting.FloatSetting("Max HP",   this, 20f,   1f,  100f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_fh_inc=" + healPerTick.getValue() + " _fh_max=" + maxHp.getValue() + "\n" +
            "_fh=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer()\n" +
            "  if p then\n" +
            "    local hp=p.Player:getHealth()\n" +
            "    if hp<_fh_max then p.Player:setHealth(math.min(hp+_fh_inc,_fh_max)) end\n" +
            "  end\n" +
            "end," + (int)delay.getValue() + ",-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _fh then LuaTimer:cancel(_fh) _fh=nil end");
    }
}