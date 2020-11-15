package me.kokumaji.Warrior.Game.Objects.Kits;

import me.kokumaji.Warrior.Game.Objects.Kit;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Soldier extends Kit {

    protected Soldier(String pName, String[] pDescription, int pID, int pCost, String pPermission, Material pKitIcon, Material pPrimary) {
        super(pName, pDescription, pID, pCost, pPermission, pKitIcon, pPrimary);
    }

    @Override
    public void GiveKit(Player p) {
        ItemStack[] armor = new ItemStack[4];

        armor[3] = new ItemStack(Material.IRON_HELMET);
        armor[2] = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        armor[1] = new ItemStack(Material.LEATHER_LEGGINGS);
        armor[0] = new ItemStack(Material.LEATHER_BOOTS);

        p.getInventory().setArmorContents(armor);

        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        swordMeta.setDisplayName("§7Stone Sword");
        sword.setItemMeta(swordMeta);

        p.getInventory().setItem(0, sword);
        p.getInventory().setItem(8, new ItemStack(Material.MUSHROOM_STEW, 32));

    }
    
}
