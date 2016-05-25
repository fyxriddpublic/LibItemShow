package com.fyxridd.lib.show.item.api;

import org.bukkit.entity.Player;

public class OptionClickEvent {
    private Player p;
    private Info info;
    private int pos;
    private boolean close;
    private boolean left;
    private boolean right;
    private boolean shift;

    public OptionClickEvent(Player p, Info info, int pos, boolean left, boolean right, boolean shift) {
        this.p = p;
        this.info = info;
        this.pos = pos;
        this.left = left;
        this.right = right;
        this.shift = shift;
    }

    public Player getP() {
        return p;
    }

    public Info getInfo() {
        return info;
    }

    public int getPos() {
        return pos;
    }

    public boolean isWillClose() {
        return close;
    }

    public void setWillClose(boolean close) {
        this.close = close;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isShift() {
        return shift;
    }
}
