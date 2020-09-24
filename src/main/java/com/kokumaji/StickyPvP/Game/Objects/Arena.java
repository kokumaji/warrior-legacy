package com.kokumaji.StickyPvP.Game.Objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.kokumaji.StickyPvP.StickyPvP;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import net.minecraft.server.v1_16_R2.WorldGenFeatureDefinedStructureJigsawPlacement.a;

public class Arena {

    private String name;
    private Region region;
    private int id;
    private Location spawn;
    private int maxPlayers;
    private int currentPlayers;

    private ArrayList<GameFlag> gameflags = new ArrayList<GameFlag>();

    public Arena(String pName, int pID, Location pSpawn, int pMaxPlayers) {
        this.name = pName;
        this.id = pID;
        this.spawn = pSpawn;
        this.maxPlayers = pMaxPlayers;
    }

    public void Save() {
        File folder = new File(StickyPvP.GetPlugin().getDataFolder() + "/arenas");
        File f = new File(folder + "/" + this.name.toLowerCase() + ".yml");

        if (!folder.exists())
            folder.mkdirs();

        if (f.exists()) {
            YamlConfiguration c = GetConfig();
            SaveLocation(c);
            try {
                c.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                f.createNewFile();
                YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
                c.set("meta.name", this.name);
                c.set("meta.id", this.id);
                c.set("meta.maxplayers", this.maxPlayers);
                SaveLocation(c);
                c.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void SaveLocation(YamlConfiguration c) {
        double x = this.spawn.getX();
        double y = this.spawn.getY();
        double z = this.spawn.getZ();
        double yaw = this.spawn.getYaw();
        double pitch = this.spawn.getPitch();
        String world = this.spawn.getWorld().getName();

        c.set("spawn.x", x);
        c.set("spawn.y", y);
        c.set("spawn.z", z);
        c.set("spawn.yaw", yaw);
        c.set("spawn.pitch", pitch);
        c.set("spawn.world", world);
    }

    public YamlConfiguration GetConfig() {
        File folder = new File(StickyPvP.GetPlugin().getDataFolder() + "/arenas");
        File f = new File(folder + "/" + this.name.toLowerCase() + ".yml");

        if(f.exists()) {
            YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
            if(c != null) return c;
        }

        return null;
    }

    public String GetName() {
        return this.name;
    }

    public int GetId() {
        return this.id;
    }

    public Region GetRegion() {
        return region;
    }

    public boolean IsInRegion(User user) {
        return region.contains(user.Bukkit().getLocation());
    }

    public Location GetSpawn() {
        return spawn;
    }

    public void SetSpawn(Location loc) {
        this.spawn = loc;
    }

    public void Teleport(User user) {
        user.Teleport(spawn);
        user.InLobby(false);
    }

    public ArrayList<GameFlag> GetGameFlags() {
        return gameflags;
    }

    public GameFlag GetGameFlag(GameFlag pType) {
        for (GameFlag gf : gameflags) {
            if(gf == pType) return gf;
        }

        return null;
    }

    public void SetGameFlag(GameFlag pType, GameFlag.State pState) {
        if(!gameflags.isEmpty()) {
            for (GameFlag gf : gameflags) {
                if(gf == pType) {
                    GameFlag gfNew = pType;
                    gf.SetState(pState);

                    gameflags.remove(gf);
                    gameflags.add(gfNew);

                    return;
                }
            }
        }

        GameFlag gf = pType;
        gf.SetState(pState);

        gameflags.add(gf);
    }

    public boolean HasGameFlag(GameFlag pType) {
        for (GameFlag gf : gameflags) {
            if(gf == pType) {
                return true;
            }
        }

        return false;
    }

}
