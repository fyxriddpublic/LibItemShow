package com.fyxridd.lib.show.item.manager;

import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.config.Setter;
import com.fyxridd.lib.func.api.FuncApi;
import com.fyxridd.lib.show.item.ShowPlugin;
import com.fyxridd.lib.show.item.api.Info;
import com.fyxridd.lib.show.item.api.OptionClickEvent;
import com.fyxridd.lib.show.item.api.OptionClickEventHandler;
import com.fyxridd.lib.show.item.config.ShowConfig;
import com.fyxridd.lib.speed.api.SpeedApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * 物品按钮<br>
 * 本身非实际功能,只是作为功能的接口
 */
public class ShowManager {
    private static final String ITEM = "item";
    private static final String SHORT_DEFAULT = "is_default";

    //计数器
    private static int id = 1;

    private ShowConfig showConfig;

    //界面ID 界面
    private Map<Integer, Info> infos = new HashMap<>();
    //玩家 正查看的界面
    private Map<Player, Info> players = new HashMap<>();

    public ShowManager() {
        //添加配置监听
        ConfigApi.addListener(ShowPlugin.instance.pn, ShowConfig.class, new Setter<ShowConfig>() {
            @Override
            public void set(ShowConfig value) {
                showConfig = value;
            }
        });
        //注册功能类型
        FuncApi.registerTypeHook(ITEM, showConfig.getItemFuncPrefix());
        //注册事件
        {
            //点击容器
            Bukkit.getPluginManager().registerEvent(InventoryClickEvent.class, ShowPlugin.instance, EventPriority.MONITOR, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    final InventoryClickEvent event = (InventoryClickEvent) e;
                    if (event.getWhoClicked() instanceof Player) {
                        final Player p = (Player) event.getWhoClicked();
                        final Info info = players.get(p);
                        if (info != null) {
                            event.setCancelled(true);
                            //速度检测
                            ItemStack is = event.getCurrentItem();
                            ItemStack is2 = event.getCursor();
                            if ((is == null || is.getType().equals(Material.AIR)) && (is2 == null || is2.getType().equals(Material.AIR))) {
                            }else if (!SpeedApi.checkShort(p, ShowPlugin.instance.pn, SHORT_DEFAULT, 2)) return;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(ShowPlugin.instance, new Runnable() {
                                @Override
                                public void run() {
                                    if (!p.isOnline()) return;
                                    int slot = event.getRawSlot();
                                    Inventory inv = info.getInv(p);
                                    if (inv == null) return;
                                    int size = inv.getSize();
                                    if (slot >= 0 && slot < size) {
                                        ItemStack result = inv.getItem(slot);
                                        if (result != null && !result.getType().equals(Material.AIR)) {
                                            OptionClickEvent optionClickEvent = new OptionClickEvent(p, info, slot, event.isLeftClick(), event.isRightClick(), event.isShiftClick());
                                            info.getHandler().onOptionClick(optionClickEvent);
                                            if (optionClickEvent.isWillClose()) {
                                                Info currentSee = players.get(p);
                                                if (currentSee != null && currentSee.equals(info)) p.closeInventory();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }, ShowPlugin.instance, true);
            //容器关闭
            Bukkit.getPluginManager().registerEvent(InventoryCloseEvent.class, ShowPlugin.instance, EventPriority.MONITOR, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    InventoryCloseEvent event = (InventoryCloseEvent) e;
                    if (event.getPlayer() instanceof Player) {
                        Player p = (Player) event.getPlayer();
                        Info info = players.get(p);
                        if (info != null) {
                            info.removePlayer(p);
                            players.remove(p);
                        }
                    }
                }
            }, ShowPlugin.instance, true);
        }
    }

    /**
     * @see com.fyxridd.lib.show.item.api.ShowApi#register(String, int, boolean, OptionClickEventHandler)
     */
    public Info register(String name, int size, boolean emptyDestroy, OptionClickEventHandler handler) {
        if (name == null) name = "";
        else if (name.length() > 32) name = name.substring(0, 32);
        int check = size % 5;
        if (check < 1) size = 9;
        else if (check > 5) size = 45;

        int id = getNextId();
        Info info = new Info(id, name, size, emptyDestroy, handler);
        infos.put(id, info);
        return info;
    }

    /**
     * @see com.fyxridd.lib.show.item.api.ShowApi#unregister(Info)
     */
    public void unregister(Info info) {
        if (info == null) return;

        info.closeAll();
        infos.remove(info.getId());
    }

    /**
     * @see com.fyxridd.lib.show.item.api.ShowApi#open(Player, Info, String, Inventory)
     */
    public void open(Player p, Info info, String title, Inventory handle) {
        if (p == null || info == null) return;

        //尝试关闭背包
        p.closeInventory();
        //关闭背包成功
        players.put(p, info);
        info.open(p, title, handle);
    }

    public Info getInfo(int id) {
        return infos.get(id);
    }

    /**
     * 获取下个可用ID
     * @return 下个可用ID
     */
    private static int getNextId() {
        return id ++;
    }
}