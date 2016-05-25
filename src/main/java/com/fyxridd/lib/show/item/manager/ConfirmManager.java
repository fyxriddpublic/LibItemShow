package com.fyxridd.lib.show.item.manager;

import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.config.Setter;
import com.fyxridd.lib.show.item.ShowPlugin;
import com.fyxridd.lib.show.item.api.Info;
import com.fyxridd.lib.show.item.api.OptionClickEvent;
import com.fyxridd.lib.show.item.api.OptionClickEventHandler;
import com.fyxridd.lib.show.item.api.ShowApi;
import com.fyxridd.lib.show.item.config.ConfirmConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfirmManager {
    private ConfirmConfig confirmConfig;

    public ConfirmManager() {
        //添加配置监听
        ConfigApi.addListener(ShowPlugin.instance.pn, ConfirmConfig.class, new Setter<ConfirmConfig>() {
            @Override
            public void set(ConfirmConfig value) {
                confirmConfig = value;
            }
        });
    }

    /**
     * @see ShowApi#confirm(Player, String, String, String)
     */
    public void confirm(final Player p, final String title, final String yesCmd, final String noCmd) {
        Info info = ShowApi.register(title, 9, true, new OptionClickEventHandler() {
            @Override
            public void onOptionClick(OptionClickEvent e) {
                Info info = e.getInfo();
                int cmd = e.getPos()-info.getInv().getSize();
                if (cmd >= 0) {//操作
                    if (cmd == confirmConfig.getYesPos()) {
                        if (yesCmd != null && !yesCmd.isEmpty()) delayCmd(p, yesCmd);
                        e.setWillClose(true);
                    }else if (cmd == confirmConfig.getNoPos()) {
                        if (noCmd != null && !noCmd.isEmpty()) delayCmd(p, noCmd);
                        e.setWillClose(true);
                    }
                }
            }
        });
        //内容栏
        ItemStack infoItem = confirmConfig.getInfoItem().clone();
        ItemMeta im = infoItem.getItemMeta();
        im.setDisplayName(title);
        infoItem.setItemMeta(im);
        info.setItem(4, infoItem);
        //操作栏
        Inventory handle = Bukkit.createInventory(p, 9, "none");
        handle.setItem(confirmConfig.getYesPos(), confirmConfig.getYesItem());
        handle.setItem(confirmConfig.getNoPos(), confirmConfig.getNoItem());
        //打开界面
        ShowApi.open(p, info, null, handle);
    }

    /**
     * 延时0执行命令
     * @param p 玩家
     * @param cmd 命令
     */
    private void delayCmd(final Player p, final String cmd) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ShowPlugin.instance, new Runnable() {
            @Override
            public void run() {
                if (p.isOnline() && !p.isDead()) p.chat(cmd);
            }
        });
    }
}
