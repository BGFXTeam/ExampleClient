package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class ShowHP extends Module {
    public ShowHP() {
        super("ShowHP", "Shows enemy HP above their head", ModuleCategory.VISUAL);
    }

    public void onEnable() {
        ScriptManager.execute(
            "_shp_orig={}\n" +
            "_shp=LuaTimer:scheduleTimer(function()\n" +
            "  local me=PlayerManager:getClientPlayer()\n" +
            "  for _,pd in ipairs(PlayerManager:getPlayers() or {}) do\n" +
            "    local p=pd.Player\n" +
            "    if p and pd~=me then\n" +
            "      local name=p:getShowName() or ''\n" +
            "      local hp=math.floor(p:getHealth()+0.5)\n" +
            "      if not _shp_orig[pd] then _shp_orig[pd]=name end\n" +
            "      if pd.lastShpHp~=hp then\n" +
            "        pd.lastShpHp=hp\n" +
            "        local parts=StringUtil.split(name,'\\n') or {}\n" +
            "        if string.find(name,'\\u2665') then table.remove(parts) end\n" +
            "        table.insert(parts,'\\u25a2FFFFFFFF'..tostring(hp)..'\\u25a2FFFF1F1F \\u2665')\n" +
            "        p:setShowName(table.concat(parts,'\\n'))\n" +
            "      end\n" +
            "    end\n" +
            "  end\n" +
            "end,50,-1)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "if _shp then LuaTimer:cancel(_shp) _shp=nil end\n" +
            "local me=PlayerManager:getClientPlayer()\n" +
            "for _,pd in ipairs(PlayerManager:getPlayers() or {}) do\n" +
            "  if pd.Player and pd~=me and _shp_orig and _shp_orig[pd] then\n" +
            "    pd.Player:setShowName(_shp_orig[pd])\n" +
            "  end\n" +
            "end\n" +
            "_shp_orig=nil"
        );
    }
}