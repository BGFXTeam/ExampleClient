package com.example.bgfx.client;

import java.util.List;
import java.util.ArrayList;

public abstract class Module {
    private String name = "Unknown";
    private String description = "Nothing";
    private ModuleCategory category;
    private boolean isEnabled = false;
    private List<Setting> settings = new ArrayList<>();

    public Module(String nm, String des, ModuleCategory cat) {
        this.name = nm;
        this.description = des;
        this.category = cat;
    }

    public void addSetting(Setting s) { settings.add(s); }
    public List<Setting> getSettings() { return settings; }

    public boolean toggle() {
        this.isEnabled = !this.isEnabled;
        if(this.isEnabled) { onEnable(); } else { onDisable(); }
        return this.isEnabled;
    }

    public boolean isEnabled() { return isEnabled; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ModuleCategory getCategory() { return category; }

    public abstract void onEnable();
    public abstract void onDisable();
}
