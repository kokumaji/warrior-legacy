package com.kokumaji.StickyPvP.Game.Managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kokumaji.StickyPvP.StickyPvP;
import com.kokumaji.StickyPvP.Game.Objects.Arena;
import com.kokumaji.StickyPvP.Utils.ConfigUtil;
import com.kokumaji.StickyPvP.Utils.InternalMessages;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ArenaManager {

    // TODO: MOVE ARENA STORAGE TO JSON FILES (./data/arenas.json)

    private static StickyPvP self = (StickyPvP) StickyPvP.GetPlugin();
    private static Map<Integer, Arena> arenaMap = new HashMap<Integer, Arena>();

    public static void LoadArenas() {
        File dir = new File(self.getDataFolder() + "/arenas");
        if(!dir.exists()) return;
        File[] files = dir.listFiles();
        for(File f : files) {
            YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
            double x = c.getDouble("spawn.x");
            double y = c.getDouble("spawn.y");
            double z = c.getDouble("spawn.z");
            double yaw = c.getDouble("spawn.yaw");
            double pitch = c.getDouble("spawn.pitch");
            World w = Bukkit.getWorld(c.getString("spawn.world"));

            Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);

            RegisterArena(new Arena(c.getString("meta.name"), c.getInt("meta.id"), loc, c.getInt("meta.maxplayers")));
        }
    }

    public static void RegisterArena(Arena a) {
        FileConfiguration c = ConfigUtil.GetConfig(ConfigUtil.ConfigType.SETTINGS);
        
        arenaMap.put(arenaMap.size(), a);
        if(c.getBoolean("general-settings.debug"))
            InternalMessages.ARENA_REGISTERED_SUCCESS.Log(a.GetName());
    }

	public static Arena GetArena(String pArena) {
        for(Arena a : arenaMap.values()) {
            if(a.GetName().toLowerCase().equals(pArena.toLowerCase())) return a;
        }
		return null;
    }
    
    public static Map<Integer, Arena> GetArenas() {
        return arenaMap;
    }

}
