package com.kokumaji.Warrior.Game.Managers;

import com.kokumaji.Warrior.Game.Objects.WarriorUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

import com.kokumaji.Warrior.Game.Objects.Kit;
import com.kokumaji.Warrior.Game.Objects.Kits.Archer;
import com.kokumaji.Warrior.Utils.InternalMessages;

public class KitManager {
    private static final ArrayList<Kit> kits = new ArrayList<>();
    private static final String[] description = {"Line 1", "Line 2"};

    private static final HashMap<Player, Long> cooldowns = new HashMap<>();

    public static void RegisterKits() {
        kits.add(new Archer("Archer", description, 0, 0, "kitpvp.kit.archer", Material.BOW, Material.BOW));

    }

    public static void addKit(Kit pKit) {
        kits.add(pKit);
    }

    public static void removeKit(Kit pKit) {
        kits.remove(pKit);
    }

    public static ArrayList<Kit> getKits() {
        return kits;
    }

    public static Kit getKitById(int pID) {
        return kits.get(pID);
    }

    public static Kit GetKit(String pName) {
        for (Kit k : kits) {
            if (k.GetName().equalsIgnoreCase(pName)) return k;
        }

        return null;
    }

    public static Kit getNext(int pID) {
        if(pID >= kits.size() - 1) return kits.get(0);
        else return kits.get(pID + 1);
    }

    public static Kit getPrevious(int pID) {
        if(pID == 0) return kits.get(kits.size() - 1);
        else return kits.get(pID - 1);
    }

    public static void SetKit(WarriorUser u, Kit k) {
        if (k != null) {
            u.bukkit().getInventory().clear();
            k.GiveKit(u.bukkit());
        } else {
            InternalMessages.KIT_NOT_FOUND.Log(u.getUsername());
        }
    }

    public static void putCooldown(Player player, Long timestamp) {
        cooldowns.put(player, timestamp);
    }

    public static Long getTimeLeft(Player player) {
        return cooldowns.get(player);
    }

    public static void removeCooldown(Player player) {
        cooldowns.remove(player);
    }

    public static boolean hasCooldown(Player player) {
        return cooldowns.containsKey(player);
    }
}
