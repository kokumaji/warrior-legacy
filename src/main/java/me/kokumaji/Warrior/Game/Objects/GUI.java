package me.kokumaji.Warrior.Game.Objects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;


public abstract class GUI implements Listener {

    private Inventory inv;
    private int s;
    private Plugin plugin;
    private Map<Integer, ClickableSlot> clickableMap;

    protected GUI(int pSize, Plugin pPlugin) {
        this.s = pSize;
        this.plugin = pPlugin;

        this.clickableMap = new HashMap<Integer, ClickableSlot>();

    }

    protected void RegisterSlot(ItemStack pItem, int pSlot) {
        if(pItem == null) 
            return;
        inv.setItem(pSlot, pItem);
    }

    protected void RegisterClickable(ClickableSlot cs) {
        if(cs.GetItem() == null || inv.getItem(cs.GetSlotId()) != null) return;
        clickableMap.put(cs.GetSlotId(), cs);
        inv.setItem(cs.GetSlotId(), cs.GetItem());
    }

    protected ClickableSlot GetClickable(int slot) {
        return clickableMap.get(slot);
    }

    protected abstract void BuildGUI(Player player);
    
    protected boolean IsValidSlot(int pSlot) {
        return inv.getItem(pSlot) != null;
    }

    protected boolean IsClickable(int pSlot) {
        return clickableMap.containsKey(pSlot);
    }

    @EventHandler
    protected abstract void onSlotClick(InventoryClickEvent e);

    protected Inventory getInventory() {
        return this.inv;
    }

    protected Plugin getPlugin() {
        return this.plugin;
    }

    protected Inventory MakeInventory(String pTitle) {
        return this.inv = Bukkit.createInventory(null, s, pTitle);
    }
}
