package com.kokumaji.Warrior.Game.Objects.GUIs;

import com.kokumaji.Warrior.Game.Objects.GUI;
import com.kokumaji.Warrior.Utils.CustomItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ClassGUI extends GUI {

    protected ClassGUI(int pSize, Plugin pPlugin) {
        super(pSize, pPlugin);
        Bukkit.getServer().getPluginManager().registerEvents(this, pPlugin);
    }

    @Override
    public void BuildGUI(Player player) {
        this.MakeInventory("Classes");
        ItemStack placeholder = CustomItem.Create(Material.BLACK_STAINED_GLASS_PANE, " ", 1, true);
        for(int i = 0; i < 9; i++) {
            RegisterSlot(placeholder, i);
            RegisterSlot(placeholder, (getInventory().getSize() - 9) +  i);
        }


        String[] fighterLore = {" ", "§7§nClass Stats:", " ", "&a+1 DMG on Melee Attacks", "&a+1 Defense", "&c-1 Attack Speed", "&8(+ Shield)", " ", "&8Some Generic Class Description."};
        ItemStack fighter = CustomItem.Create(Material.IRON_SWORD, "§3§l§oFighter §b★☆☆☆☆", 1, true, fighterLore);

        String[] warriorLore = {" ", "§7§nClass Stats:", " ", "&a+2 DMG on Melee Attacks", "&a+2 Defense", "&c-2 Walk Speed", "&c-2 Attack Speed", " ", "&8Some Generic Class Description."};
        ItemStack warrior = CustomItem.Create(Material.IRON_CHESTPLATE, "§3§l§oWarrior §b★☆☆☆☆", 1, true, warriorLore);

        String[] hunterLore = {" ", "§7§nClass Stats:", " ", "&a+2 DMG on Range Attacks", "&a+2 Walk Speed", "&c-2 Armor", "&c-4 DMG on Melee Attacks", " ", "&8Some Generic Class Description."};
        ItemStack hunter = CustomItem.Create(Material.BOW, "§3§l§oHunter §b★☆☆☆☆", 1, true, hunterLore);

        String[] wizardLore = {" ", "§7§nClass Stats:", " ", "&a+3 DMG on Range Attacks", "&a+1 Walk Speed", "&c-2 Defense", "&c-3 Attack Speed", " ", "&8Some Generic Class Description."};
        ItemStack wizard = CustomItem.Create(Material.ENCHANTING_TABLE, "§3§l§oWizard §b★☆☆☆☆", 1, true, wizardLore);

        RegisterSlot(fighter, 10);
        RegisterSlot(warrior, 12);
        RegisterSlot(hunter, 14);
        RegisterSlot(wizard, 16);
        player.openInventory(getInventory());
    }

    @Override
    protected void onSlotClick(InventoryClickEvent inventoryClickEvent) {

    }
}
