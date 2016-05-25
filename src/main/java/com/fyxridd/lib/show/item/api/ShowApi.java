package com.fyxridd.lib.show.item.api;

import com.fyxridd.lib.show.item.ShowPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ShowApi {
    /**
     * 注册界面
     * @param name 容器名(最长32字符,超过时会自动截断),可为null
     * @param size 大小,9的1-5倍(不在范围内时会自动调整)
     * @param emptyDestroy 指界面没有观看者后是否自动注销
     * @param handler 点击事件处理器,不为null
     * @return 注册的界面信息
     */
    public static Info register(String name, int size, boolean emptyDestroy, OptionClickEventHandler handler) {
        return ShowPlugin.instance.getShowManager().register(name, size, emptyDestroy, handler);
    }

    /**
     * 注销界面
     * @param info 界面,可为null
     */
    public static void unregister(Info info) {
        ShowPlugin.instance.getShowManager().unregister(info);
    }

    /**
     * 让玩家打开指定界面
     * @param p 玩家,可为null
     * @param info 界面,可为null
     * @param title null表示显示原标题
     * @param handle 9格的操作栏,null表示不显示操作栏
     */
    public static void open(Player p, Info info, String title, Inventory handle) {
        ShowPlugin.instance.getShowManager().open(p, info, title, handle);
    }

    /**
     * 用ID获取界面信息(可用来检测某个Info是否已经销毁)
     * @param id 界面ID
     * @return 界面信息,没有返回null
     */
    public static Info getInfo(int id) {
        return ShowPlugin.instance.getShowManager().getInfo(id);
    }

    /**
     * 确认
     * @param p 玩家
     * @param title 标题
     * @param yesCmd 点击确认执行的命令(可为null)
     * @param noCmd 点击取消执行的命令(可为null)
     */
    public static void confirm(Player p, String title, String yesCmd, String noCmd) {
        ShowPlugin.instance.getConfirmManager().confirm(p, title, yesCmd, noCmd);
    }
}
