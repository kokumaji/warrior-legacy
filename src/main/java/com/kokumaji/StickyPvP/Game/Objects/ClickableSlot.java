package com.kokumaji.StickyPvP.Game.Objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClickableSlot {
    
    private ItemStack is;
    private int slot;

    public ClickableSlot(Material pMaterial, String pName, int pAmount, int pSlot) {
        this.is = new ItemStack(pMaterial, pAmount);

        ItemMeta isM = is.getItemMeta();
        isM.setDisplayName(pName);
        is.setItemMeta(isM);

        this.slot = pSlot;
    }

    public void Execute(Runnable r) {
        new Thread(r).start();
    }

    public ItemMeta GetMeta() {
        return is.getItemMeta();
    }

    public ItemStack GetItem() {
        return is;
    }

    public int GetSlotId() {
        return slot;
    }

    public void SetName(String s) {
        ItemMeta isM = this.is.getItemMeta();
        isM.setDisplayName(s);
        is.setItemMeta(isM);
    }

    public String GetName() {
        return is.getItemMeta().getDisplayName();
    }
}
