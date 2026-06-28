package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", "Teleports back to last safe position on void fall", ModuleCategory.MOVEMENT);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_av_orig=nil\n" +
            // check timer: if falling hard, warp back
            "_av=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer().Player\n" +
            "  local pos=p:getPosition()\n" +
            "  local fd=p.fallDistance\n" +
            "  if fd==0 and not _av_orig then _av_orig=pos end\n" +
            "  if fd>=10 and _av_orig then p:setPosition(_av_orig) end\n" +
            "end,100,-1)\n" +
            // save timer: update safe pos every 5s when grounded
            "_av_sv=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer().Player\n" +
            "  if p.fallDistance==0 then _av_orig=p:getPosition() end\n" +
            "end,5000,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _av   then LuaTimer:cancel(_av)   _av=nil   end\n" +
            "if _av_sv then LuaTimer:cancel(_av_sv) _av_sv=nil end\n" +
            "_av_orig=nil"
        );
    }
}