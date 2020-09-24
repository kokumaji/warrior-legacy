package com.kokumaji.Warrior.Game.Objects;

import com.kokumaji.Warrior.Game.Managers.UserManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class Kit implements Listener {

    private final String kitName;
    private final String[] kitDescription;
    private final int kitCost;
    private final String kitPermission;
    private final Material kitIcon;
    private final int kitID;
    private final Material primary;

    protected Kit(String pName, String[] pDescription, int pID, int pCost, String pPermission, Material pKitIcon, Material pPrimary) {
        this.kitName = pName;
        this.kitDescription = pDescription;
        this.kitID = pID;
        this.kitCost = pCost;
        this.kitPermission = pPermission;
        this.kitIcon = pKitIcon;
        this.primary = pPrimary;
    }

    public String GetName() {
        return kitName;
    }

    protected String[] GetDescription() {
        return kitDescription;
    }

    protected int GetCost() {
        return kitCost;
    }

    protected int GetId() {
        return kitID;
    }

    protected String GetPermission() {
        return kitPermission;
    }

    protected Material KitIcon() {
        return kitIcon;
    }

    protected Material Primary() {
        return primary;
    }

    public void ExecuteSpecial(Runnable r) {
        new Thread(r).start();
    }

    public abstract void GiveKit(Player p);

}
