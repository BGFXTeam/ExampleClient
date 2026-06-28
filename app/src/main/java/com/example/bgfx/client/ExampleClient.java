package com.example.bgfx.client;

import com.executor.plugin.Plugin;
import com.executor.plugin.PluginContext;
import android.content.Context;

public class ExampleClient extends Plugin {
    public static Context ctx;

    @Override
    public void onLoad(PluginContext pluginContext) {
        this.ctx = pluginContext.getContext();

        new ClickGUI(ctx);
    }
}
