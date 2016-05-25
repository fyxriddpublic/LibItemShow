package com.fyxridd.lib.show.item.config;

import com.fyxridd.lib.core.api.config.basic.Path;

@Path("show")
public class ShowConfig {
    @Path("itemFuncPrefix")
    private String itemFuncPrefix;

    public String getItemFuncPrefix() {
        return itemFuncPrefix;
    }
}
