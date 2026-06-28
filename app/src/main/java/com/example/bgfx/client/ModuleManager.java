package com.example.bgfx.client;
import com.example.bgfx.client.impl.Aimbot;
import com.example.bgfx.client.impl.AntiAFK;
import com.example.bgfx.client.impl.AntiKnockback;
import com.example.bgfx.client.impl.AntiVoid;
import com.example.bgfx.client.impl.AttackReach;
import com.example.bgfx.client.impl.AutoClicker;
import com.example.bgfx.client.impl.Blink;
import com.example.bgfx.client.impl.Cannon;
import com.example.bgfx.client.impl.ClickTP;
import com.example.bgfx.client.impl.CriticalHit;
import com.example.bgfx.client.impl.FastBreak;
import com.example.bgfx.client.impl.FastHeal;
import com.example.bgfx.client.impl.Fly;
import com.example.bgfx.client.impl.FlyV2;
import com.example.bgfx.client.impl.Fullbright;
import com.example.bgfx.client.impl.GetPoints;
import com.example.bgfx.client.impl.GodMode;
import com.example.bgfx.client.impl.HighJump;
import com.example.bgfx.client.impl.Hitbox;
import com.example.bgfx.client.impl.InfGcubes;
import com.example.bgfx.client.impl.JetPack;
import com.example.bgfx.client.impl.KillAura;
import com.example.bgfx.client.impl.NoDelay;
import com.example.bgfx.client.impl.NoFall;
import com.example.bgfx.client.impl.NoSlow;
import com.example.bgfx.client.impl.Nuker;
import com.example.bgfx.client.impl.Parachute;
import com.example.bgfx.client.impl.Reach;
import com.example.bgfx.client.impl.SavePos;
import com.example.bgfx.client.impl.Scaffold;
import com.example.bgfx.client.impl.ShowHP;
import com.example.bgfx.client.impl.Skate;
import com.example.bgfx.client.impl.Speed;
import com.example.bgfx.client.impl.TeamPoints;
import com.example.bgfx.client.impl.TriggerBot;
import com.example.bgfx.client.impl.WatchMode;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static ModuleManager INSTANCE = new ModuleManager();
    private List<Module> modules = new ArrayList<>();
    
    public ModuleManager() {
        // ── Combat ────────────────────────────────────────────────────────────
        addMod(new Aimbot());        // yaw/pitch lock on nearest enemy via SceneManager camera
        addMod(new KillAura());      // auto-attackEntity loop with range + team filter
        addMod(new AttackReach());   // teleport-to-entity on hit, hitbox expand, teleport back
        addMod(new Hitbox());        // expands all enemy Player.height/width/length
        addMod(new NoDelay());       // banClickCD / 0CDClick / Bedward0CDClick prefs
        addMod(new AutoClicker());   // handleTouchClick at configurable CPS
        addMod(new CriticalHit());   // micro Y-drop before each hit to crit register
        addMod(new AntiKnockback()); // saves pos, restores lerped toward saved pos after hit
        addMod(new TriggerBot());    // clicks only when an enemy is within range

        // ── Movement ──────────────────────────────────────────────────────────
        addMod(new Fly());           // setAllowFlying + moveEntity (dev fly)
        addMod(new FlyV2());         // EnableDoubleJumps + infinite doubleJumpCount
        addMod(new JetPack());       // directional setVelocity from yaw/pitch
        addMod(new Speed());         // setSpeedAdditionLevel on tight timer
        addMod(new NoFall());        // tracks Y delta, toggles noClip only during fall
        addMod(new AntiVoid());      // saves last grounded pos, warps back at fallDistance>=10
        addMod(new Blink());         // SyncClientPositionToServer=false with optional auto-sync
        addMod(new ClickTP());       // ClickToBlockEvent -> setPosition
        addMod(new Skate());         // DisableUpdateAnimState=true
        addMod(new HighJump());      // velocity Y boost on leaving ground
        addMod(new NoSlow());        // setSprinting + SprintLimitCheck pref
        addMod(new SavePos());       // enable=save, disable=teleport back

        // ── World ─────────────────────────────────────────────────────────────
        addMod(new Reach());         // BlockReachDistance + EntityReachDistance prefs
        addMod(new FastBreak());     // setHardness(0) on all block IDs, restores on disable
        addMod(new Nuker());         // destroyBlock in configurable radius loop
        addMod(new Scaffold());      // placeBlock under feet on timer

        // ── Visual ────────────────────────────────────────────────────────────
        addMod(new ShowHP());        // setShowName with HP+heart suffix on 50ms timer
        addMod(new Fullbright());    // Brightness pref

        // ── Misc ──────────────────────────────────────────────────────────────
        addMod(new GetPoints());     // sendOtherTips scoring packet
        addMod(new InfGcubes());     // wallet.m_diamondGolds + LocalGcube pref
        addMod(new GodMode());       // continuously setHealth to max
        addMod(new AntiAFK());       // periodic handleTouchClick to avoid AFK kick
        addMod(new FastHeal());      // fast incremental HP regen timer
        addMod(new WatchMode());     // setWatchMode + fly
        addMod(new Parachute());     // startParachute one-shot
        addMod(new Cannon());        // setVelocity in look dir + optional parachute
        addMod(new TeamPoints());    // repeated scoring packet burst
        
    }
    
    public void addMod(Module m){modules.add(m);}
    public List<Module> getModules() {return modules;}
    public List<Module> getEnabledMod() {
        List<Module> array = new ArrayList<>();
        for (Module m : modules) {
            if (m.isEnabled()) {
                array.add(m);
            }
        }
        return array;
    }
}
