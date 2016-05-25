package com.fyxridd.lib.show.item;

import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.plugin.SimplePlugin;
import com.fyxridd.lib.show.item.config.ConfirmConfig;
import com.fyxridd.lib.show.item.config.ShowConfig;
import com.fyxridd.lib.show.item.manager.ConfirmManager;
import com.fyxridd.lib.show.item.manager.ShowManager;

public class ShowPlugin extends SimplePlugin{
    public static ShowPlugin instance;

    private ShowManager showManager;
    private ConfirmManager confirmManager;

    @Override
    public void onEnable() {
        instance = this;

        //注册配置
        ConfigApi.register(pn, ShowConfig.class);
        ConfigApi.register(pn, ConfirmConfig.class);

        showManager = new ShowManager();
        confirmManager = new ConfirmManager();

        super.onEnable();
    }

    public ShowManager getShowManager() {
        return showManager;
    }

    public ConfirmManager getConfirmManager() {
        return confirmManager;
    }
}