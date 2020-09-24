package com.kokumaji.StickyPvP.Game.Objects.Kits;

import com.kokumaji.StickyPvP.Game.Objects.Kit;
import com.kokumaji.StickyPvP.Utils.CustomItem;
import com.kokumaji.StickyPvP.Utils.LoreFormatter;
import com.kokumaji.StickyPvP.Utils.CustomItem.LimiterType;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Archer extends Kit {

    public Archer(String pName, String[] pDescription, int pID, int pCost, String pPermission, Material pKitIcon, Material pPrimary) {
        super(pName, pDescription, pID, pCost, pPermission, pKitIcon, pPrimary);
    }

    @Override
    public void GiveKit(Player p) {
        ItemStack[] armor = new ItemStack[4];

        armor[3] = new ItemStack(Material.LEATHER_HELMET);
        armor[2] = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        armor[1] = new ItemStack(Material.LEATHER_LEGGINGS);
        armor[0] = new ItemStack(Material.LEATHER_BOOTS);

        p.getInventory().setArmorContents(armor);

        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);

        ItemMeta bowMeta = bow.getItemMeta();
        ItemMeta swordMeta = sword.getItemMeta();

        LoreFormatter lfSword = new LoreFormatter("§8Attack_Damage: §f5 DMG", "§8Enchantment: §fSharpness 1", "§8Activate_Special:");
        LoreFormatter lfBow = new LoreFormatter("§8Activate_Special:");

        bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        bowMeta.setDisplayName("§7Basic Bow");
        bowMeta.setLore(lfBow.GetFormatted());
        bowMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        swordMeta.setDisplayName("§7Basic Sword");
        swordMeta.setLore(lfSword.GetFormatted());
        swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        swordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        bow.setItemMeta(bowMeta);
        sword.setItemMeta(swordMeta);

        p.getInventory().setItem(0, bow);
        p.getInventory().setItem(1, sword);
        p.getInventory().setItem(7, CustomItem.GiveLimiter(LimiterType.ARROW));
        p.getInventory().setItem(8, CustomItem.GiveLimiter(LimiterType.MUSHROOM_STEW));
    }
    
}
