package com.fyxridd.lib.show.item;

import com.fyxridd.lib.core.api.plugin.SimplePlugin;

public class ShowPlugin extends SimplePlugin{
    public static ShowPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        super.onEnable();
    }
}