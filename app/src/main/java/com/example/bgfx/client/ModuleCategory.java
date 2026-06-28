package com.example.bgfx.client;

public enum ModuleCategory {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    WORLD("World"),
    VISUAL("Visual"),
    MISC("Misc");

    private final String category;

    ModuleCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return category;
    }
}
