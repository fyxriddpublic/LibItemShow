package com.fyxridd.lib.show.item.api;

import com.fyxridd.lib.show.item.ShowPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Info {
    //界面ID
    private int id;
    //名字
    private String name;
    //界面
    private Inventory inv;
    //空的格子数量
    private int emptySlots;
    //此界面点击事件的处理者
    private OptionClickEventHandler handler;
    //true表示当没人查看此界面时删除此界面
    private boolean emptyDestroy;
    private Map<Player, Inventory> invs;

    public Info(int id, String name, int size, boolean emptyDestroy, OptionClickEventHandler handler) {
        super();
        this.id = id;
        this.name = name;
        this.inv = Bukkit.createInventory(null, size, name);
        this.emptySlots = size;
        this.emptyDestroy = emptyDestroy;
        this.handler = handler;
        this.invs = new HashMap<>();
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    /**
     * 获取界面原未加操作栏的界面<br>
     * 勿直接设置容器内的物品!!!<br>
     * 设置物品请用setItem方法
     * @return 未加操作栏的界面
     */
    public Inventory getInv() {
        return inv;
    }

    /**
     * 获取玩家的界面<br>
     * 勿直接设置容器内的物品!!!<br>
     * 设置物品请用setItem方法
     * @return null表示没有
     */
    public Inventory getInv(Player p) {
        return invs.get(p);
    }

    public int getEmptySlots() {
        return emptySlots;
    }

    /**
     * 设置物品(所有容器内的物品设置都必须通过此方法调用)<br>
     * 在所有的设置物品完成后请调用update()方法更新
     * @param slot 位置
     * @param is 新的物品
     */
    public void setItem(int slot, ItemStack is) {
        if (inv.getItem(slot) == null || inv.getItem(slot).getType().equals(Material.AIR)) {
            if (is != null) emptySlots ++;
        }else {
            if (is == null) emptySlots --;
        }
        inv.setItem(slot, is);
        for (Inventory inv: invs.values()) inv.setItem(slot, is);
    }

    /**
     * 更新所有查看此界面的玩家背包
     */
    public void update() {
        for (Player p: invs.keySet()) {
            try {
                p.updateInventory();
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    /**
     * 关闭所有正查看此界面的玩家界面
     */
    public void closeAll() {
        for (Player p: invs.keySet()) {
            p.closeInventory();
        }
    }

    public boolean isEmptyDestroy() {
        return emptyDestroy;
    }

    public void setEmptyDestroy(boolean emptyDestroy) {
        this.emptyDestroy = emptyDestroy;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Info)obj).getId() == id;
    }

    public OptionClickEventHandler getHandler() {
        return handler;
    }

    /**
     * 玩家不再查看此界面时调用
     * @param p 玩家,不为null
     */
    public void removePlayer(Player p) {
        invs.remove(p);
        if (emptyDestroy && invs.isEmpty()) ShowPlugin.instance.getShowManager().unregister(this);
    }

    /**
     * 让玩家打开界面
     * @param p 玩家
     * @param title null表示显示原标题
     * @param handle 9格的操作栏,如果为null表示不显示操作栏
     */
    public void open(Player p, String title, Inventory handle) {
        int size = inv.getSize();
        if (handle != null) size += 9;
        if (title == null) title = inv.getTitle();
        Inventory result = Bukkit.createInventory(p, size, title);
        invs.put(p, result);
        //设置界面
        if (handle != null) {
            for (int i=0;i<size-9;i++) result.setItem(i, inv.getItem(i));
            for (int i=0;i<9;i++) result.setItem(size-9+i, handle.getItem(i));
        }else {
            for (int i=0;i<size;i++) result.setItem(i, inv.getItem(i));
        }
        p.openInventory(result);
    }
}
