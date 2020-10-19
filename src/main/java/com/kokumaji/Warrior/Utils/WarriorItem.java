package com.kokumaji.Warrior.Utils;

import com.kokumaji.Warrior.Warrior;
import lombok.Getter;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.naming.Name;
import java.util.Arrays;

public class WarriorItem {

    @Getter
    private Material type;

    private int amount;
    private ItemMeta itemMeta;

    private ItemStack finalItem;

    public WarriorItem(Material itemType, int itemAmount) {
        this.type = itemType;
        this.amount = itemAmount;

        finalItem = new ItemStack(type, amount);
        this.itemMeta = finalItem.getItemMeta();
        hideFlags(true);
    }


    public WarriorItem(Material itemType, int itemAmount, String name) {
        this.type = itemType;
        this.amount = itemAmount;


        finalItem = new ItemStack(type, amount);
        this.itemMeta = finalItem.getItemMeta();
        hideFlags(true);
        setName(name);
    }

    public WarriorItem setName(String itemName) {
        if(itemName != null && itemName.length() < 36)
            this.itemMeta.setDisplayName(Translator.applyColor(itemName));

        return this;
    }

    public WarriorItem setLore(String... itemLore) {
        this.itemMeta.setLore(Arrays.asList(itemLore));

        return this;
    }

    public WarriorItem setDurability(int itemDamage, boolean unbreakable) {
        if(finalItem instanceof Damageable) {
            ((Damageable)itemMeta).setDamage(itemDamage);
            itemMeta.setUnbreakable(unbreakable);
        }

        return this;
    }

    public WarriorItem addEnchant(Enchantment enchantment, int level, boolean ignoreRestriction) {
        if(ignoreRestriction) {
            itemMeta.addEnchant(enchantment, level, true);
        } else {
            itemMeta.addEnchant(enchantment, level, false);
        }

        return this;
    }

    public WarriorItem hideFlags(boolean hideFlags) {
        if(hideFlags)
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        return this;
    }

    public WarriorItem addFlags(ItemFlag... itemFlag) {
        this.itemMeta.addItemFlags(itemFlag);

        return this;
    }

    public WarriorItem setAttackDamage(double damage) {
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        NamespacedKey damageKey = new NamespacedKey(Warrior.GetPlugin(), "attackDamage");
        if(!container.has(damageKey, PersistentDataType.DOUBLE))
            container.set(damageKey, PersistentDataType.DOUBLE, damage);

        return this;
    }

    public WarriorItem setRange(double damage) {
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        NamespacedKey rangeKey = new NamespacedKey(Warrior.GetPlugin(), "rangeValue");
        if(!container.has(rangeKey, PersistentDataType.DOUBLE))
            container.set(rangeKey, PersistentDataType.DOUBLE, damage);

        return this;
    }

    public ItemStack build() {
        finalItem.setItemMeta(this.itemMeta);
        return finalItem;
    }

}
