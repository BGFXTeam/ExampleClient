package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class FastBreak extends Module {
    private final Setting.FloatSetting maxId;

    public FastBreak() {
        super("FastBreak", "Sets all block hardness to 0 for instant break", ModuleCategory.WORLD);
        maxId = new Setting.FloatSetting("Max Block ID", this, 40000f, 100f, 40000f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_fb_orig={}\n" +
            "for bid=1," + (int)maxId.getValue() + " do\n" +
            "  local b=BlockManager.getBlockById(bid)\n" +
            "  if b then\n" +
            "    _fb_orig[bid]=b:getHardness()\n" +
            "    b:setHardness(0)\n" +
            "  end\n" +
            "end"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _fb_orig then\n" +
            "  for bid,h in pairs(_fb_orig) do\n" +
            "    local b=BlockManager.getBlockById(bid)\n" +
            "    if b then b:setHardness(h) end\n" +
            "  end\n" +
            "  _fb_orig=nil\n" +
            "end"
        );
    }
}