package com.example.bgfx.client.impl;

import com.example.bgfx.client.Module;
import com.example.bgfx.client.ModuleCategory;
import com.example.bgfx.client.Setting;
import com.executor.bgfxui.ScriptManager;

public class AttackReach extends Module {
    private final Setting.FloatSetting  entityReach;
    private final Setting.FloatSetting  hitboxSize;
    private final Setting.FloatSetting  checkDelay;

    public AttackReach() {
        super("AttackReach", "Teleport-bypass on attack for max reach", ModuleCategory.COMBAT);
        entityReach = new Setting.FloatSetting("Entity Reach", this, 999f, 10f, 9999f);
        hitboxSize  = new Setting.FloatSetting("Hitbox Size",  this, 3f,   1f,  10f);
        checkDelay  = new Setting.FloatSetting("Check ms",     this, 300f, 50f, 1000f);
    }

    public void onEnable() {
        ScriptManager.execute(
            "ClientHelper.putFloatPrefs('EntityReachDistance'," + entityReach.getValue() + ")\n" +
            "_ar_hs=" + hitboxSize.getValue() + " _ar_cd=" + (int)checkDelay.getValue() + "\n" +
            "_ar_savedPos=nil\n" +
            "CEvents.AttackEntityEvent:registerCallBack(function(eid)\n" +
            "  local entity=PlayerManager:getPlayerByEntityId(eid)\n" +
            "  if entity then\n" +
            "    _ar_savedPos=PlayerManager:getClientPlayer().Player:getPosition()\n" +
            "    ClientHelper.putBoolPrefs('SyncClientPositionToServer',true)\n" +
            "    PlayerManager:getClientPlayer().Player:setPosition(entity:getPosition())\n" +
            "    entity.height=_ar_hs entity.width=_ar_hs entity.length=_ar_hs\n" +
            "    local initHP=entity:getHP()\n" +
            "    LuaTimer:scheduleTimer(function()\n" +
            "      if entity:getHP()<initHP then\n" +
            "        if _ar_savedPos then PlayerManager:getClientPlayer().Player:setPosition(_ar_savedPos) end\n" +
            "        ClientHelper.putBoolPrefs('SyncClientPositionToServer',false)\n" +
            "        entity.height=1.8 entity.width=0.6 entity.length=0.6\n" +
            "      end\n" +
            "    end,1,_ar_cd)\n" +
            "  end\n" +
            "end)"
        );
    }

    public void onDisable() {
        ScriptManager.execute(
            "ClientHelper.putFloatPrefs('EntityReachDistance',5)\n" +
            "ClientHelper.putBoolPrefs('SyncClientPositionToServer',true)\n" +
            "_ar_savedPos=nil"
        );
    }
}