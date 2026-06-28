package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class Nuker extends Module {
    private final Setting.FloatSetting radius;
    private final Setting.FloatSetting delay;

    public Nuker() {
        super("Nuker", "Auto-destroys blocks in a radius", ModuleCategory.WORLD);
        radius = new Setting.FloatSetting("Radius", this, 3f, 1f, 8f);
        delay  = new Setting.FloatSetting("Delay ms", this, 200f, 50f, 1000f);
    }

    public void onEnable() {
        int r  = (int) radius.getValue();
        int dl = (int) delay.getValue();
        ScriptManager.execute(
            "_nk_r=" + r + "\n" +
            "_nk=LuaTimer:scheduleTimer(function()\n" +
            "  local pos=PlayerManager:getClientPlayer().Player:getPosition()\n" +
            "  for x=-_nk_r,_nk_r do for y=-_nk_r,_nk_r do for z=-_nk_r,_nk_r do\n" +
            "    local bp=VectorUtil.newVector3(math.floor(pos.x)+x,math.floor(pos.y)+y,math.floor(pos.z)+z)\n" +
            "    local b=BlockManager:getBlock(bp)\n" +
            "    if b and b.hardness and b.hardness>0 then BlockManager:destroyBlock(bp) end\n" +
            "  end end end\n" +
            "end," + dl + ",-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute("if _nk then LuaTimer:cancel(_nk) _nk=nil end");
    }
}