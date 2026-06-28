package com.example.bgfx.client;

import java.util.ArrayList;
import java.util.List;

public abstract class Setting {
    private String name;
    private Module module;

    public Setting(String name, Module module) {
        this.name = name;
        this.module = module;
        if (module != null) {
            module.addSetting(this);
        }
    }

    public String getName() { return name; }
    public Module getModule() { return module; }

    public static class BooleanSetting extends Setting {
        private boolean value;

        public BooleanSetting(String name, Module module, boolean value) {
            super(name, module);
            this.value = value;
        }

        public boolean getValue() { return value; }
        public void setValue(boolean value) { this.value = value; }
    }

    public static class FloatSetting extends Setting {
        private float value, min, max;

        public FloatSetting(String name, Module module, float value, float min, float max) {
            super(name, module);
            this.value = value;
            this.min = min;
            this.max = max;
        }

        public float getValue() { return value; }
        public void setValue(float value) { this.value = Math.max(min, Math.min(max, value)); }
        public float getMin() { return min; }
        public float getMax() { return max; }
    }
}
