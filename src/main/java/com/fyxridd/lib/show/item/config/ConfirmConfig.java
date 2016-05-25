package com.fyxridd.lib.show.item.config;

import com.fyxridd.lib.core.api.ItemApi;
import com.fyxridd.lib.core.api.UtilApi;
import com.fyxridd.lib.core.api.config.basic.Path;
import com.fyxridd.lib.core.api.config.convert.ConfigConvert;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Path("confirm")
public class ConfirmConfig {
    private class ItemConverter implements ConfigConvert.ConfigConverter<ItemStack> {
        @Override
        public ItemStack convert(String plugin, ConfigurationSection config) throws Exception {
            int id, smallId;
            String[] temp = config.getString("item").split(":", 2);
            if (temp.length == 2) {
                id = Integer.parseInt(temp[0]);
                smallId = Integer.parseInt(temp[1]);
            }else {
                id = Integer.parseInt(temp[0]);
                smallId = 0;
            }
            ItemStack result = new ItemStack(id, 1, (short)smallId);
            String displayName = config.getString("name");
            if (displayName != null) {
                ItemMeta im = ItemApi.EmptyIm.clone();
                im.setDisplayName(UtilApi.convert(displayName));
                result.setItemMeta(im);
            }
            return result;
        }
    }

    @Path("approve.pos")
    private int yesPos;

    @Path("cancel.pos")
    private int noPos;

    @Path("info")
    @ConfigConvert(ItemConverter.class)
    private ItemStack infoItem;

    @Path("approve")
    @ConfigConvert(ItemConverter.class)
    private ItemStack yesItem;

    @Path("cancel")
    @ConfigConvert(ItemConverter.class)
    private ItemStack noItem;

    public int getYesPos() {
        return yesPos;
    }

    public int getNoPos() {
        return noPos;
    }

    public ItemStack getInfoItem() {
        return infoItem;
    }

    public ItemStack getYesItem() {
        return yesItem;
    }

    public ItemStack getNoItem() {
        return noItem;
    }
}
