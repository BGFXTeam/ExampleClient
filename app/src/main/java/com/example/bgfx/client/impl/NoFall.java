package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class NoFall extends Module {
    private final Setting.FloatSetting tolerance;

    public NoFall() {
        super("NoFall", "Cancels fall damage by detecting Y drop and toggling noClip", ModuleCategory.MOVEMENT);
        tolerance = new Setting.FloatSetting("Tolerance", this, 1.5f, 0.5f, 5f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_nf_tol=" + tolerance.getValue() + " _nf_prevY=nil\n" +
            "_nf=LuaTimer:scheduleTimer(function()\n" +
            "  local p=PlayerManager:getClientPlayer().Player\n" +
            "  local y=p:getPosition().y\n" +
            "  if _nf_prevY==nil then _nf_prevY=y return end\n" +
            "  if y<_nf_prevY-_nf_tol then\n" +
            "    p.noClip=true\n" +
            "  else\n" +
            "    p.noClip=false\n" +
            "  end\n" +
            "  _nf_prevY=y\n" +
            "end,100,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _nf then LuaTimer:cancel(_nf) _nf=nil end\n" +
            "PlayerManager:getClientPlayer().Player.noClip=false\n" +
            "_nf_prevY=nil"
        );
    }
}