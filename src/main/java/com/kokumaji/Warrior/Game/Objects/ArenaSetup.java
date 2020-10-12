package com.kokumaji.Warrior.Game.Objects;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kokumaji.Warrior.Game.Managers.ArenaManager;
import com.kokumaji.Warrior.Utils.CustomItem;
import com.kokumaji.Warrior.Utils.MessageUtil;

public class ArenaSetup {

    class SetupUser extends WarriorUser {

        int setupStep;
        String arenaName;

        Inventory inv;

        Location arenaPos1;
        Location arenaPos2;
        Region arenaRegion;

        Location arenaSpawn; 

        SetupUser(WarriorUser pUser, String pArenaName) {
            super(pUser.bukkit(), pUser.getStats());
            setupStep = 0;

            this.inv = pUser.bukkit().getInventory();
            pUser.bukkit().getInventory().clear();

            setupMap.put(pUser.getUUID(), this);
            this.arenaName = pArenaName;
        }

        int GetStep() {
            return setupStep;
        }

        void NextStep() {
            setupStep++;
        }

        void SetArenaRegion(Region region) {
            this.arenaRegion = region;
        }

        void SetArenaPos1(Location loc) {
            this.arenaPos1 = loc;
        }

        void SetArenaPos2(Location loc) {
            this.arenaPos2 = loc;
        }

        Location GetArenaPos1() {
            return arenaPos1;
        }

        Location GetArenaPos2() {
            return arenaPos2;
        }

    }

    private static HashMap<UUID, SetupUser> setupMap = new HashMap<UUID, SetupUser>();

    public ArenaSetup(String pArena, WarriorUser pUser) {
        if(IsSetupUser(pUser)) {
            //pUser.SendMessage(TranslationsUtil.Translate(pUser.Bukkit(), "arena-setup.already-in-setup", true));
            return;
        }

        if(ArenaManager.GetArena(pArena) != null) {
            //pUser.SendMessage(TranslationsUtil.Translate(pUser.Bukkit(), "arena-setup.arena-already-exists", true));
        }

        InitSetup(new SetupUser(pUser, pArena));
    }

    private void InitSetup(SetupUser setupUser) {
        MessageUtil.CenterMessage(setupUser.bukkit(), MessageUtil.HL(8) + " &8[ &3&lArena Setup &8] " + MessageUtil.HL(8), "&7Now you have to define the arena region.", "&7Use the &bArena Region Wand &7to define", "&bPosition 1 &7and &bPosition 2 &7of your arena.");
        GiveTool(setupUser);
        setupUser.NextStep();
        //TODO: how to handle next steps???
    }

    private void GiveTool(SetupUser setupUser) {
        ItemStack tool = CustomItem.Create(Material.STICK, "&bArena Region Wand", 1, true, "&7Use this tool to define", "&7the region for your arena");
        ItemStack exit = CustomItem.Create(Material.BARRIER, "&cCancel Arena Creation", 1, true, "&7Use this tool to cancel", "&7the arena creation progress.");
        setupUser.bukkit().getInventory().setItem(0, tool);
        setupUser.bukkit().getInventory().setItem(0, exit);
    }

    public static boolean IsSetupUser(WarriorUser user) {
        for(SetupUser su : setupMap.values()) {
            if(su.getUUID().equals(user.getUUID())) return true;
        }

        return false;
    }
    
}