package com.kokumaji.Warrior.Game.Objects.GUIs;

import com.kokumaji.Warrior.Game.Objects.GUI;
import com.kokumaji.Warrior.Utils.CustomItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ArenaGUI extends GUI {

    public ArenaGUI(int pSize, Plugin pPlugin) {
        super(pSize, pPlugin);
        Bukkit.getServer().getPluginManager().registerEvents(this, pPlugin);
    }

    @Override
    public void BuildGUI(Player player) {
        this.MakeInventory("Arenas");
        ItemStack placeholder = CustomItem.Create(Material.BLACK_STAINED_GLASS_PANE, " ", 1, true);
        for(int i = 0; i < 9; i++) {
            RegisterSlot(placeholder, i);
            RegisterSlot(placeholder, (getInventory().getSize() - 9) +  i);
        }

        player.openInventory(getInventory());

    }

    @Override
    protected void onSlotClick(InventoryClickEvent e) {
        // TODO Auto-generated method stub

    }
    
}
