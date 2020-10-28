package me.kokumaji.Warrior.Game.Managers;

import me.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.Warrior.Utils.ConfigUtil;
import me.kokumaji.HibiscusAPI.api.objects.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;

public class LobbyManager {

    private FileConfiguration c;
    private Plugin pl;
    private Location spawn;

    public LobbyManager(Plugin plugin) {
        // TODO: MOVE LOBBY SETTINGS TO JSON FILES (./data/lobby.json)
        this.c = ConfigUtil.GetConfig(ConfigUtil.ConfigType.LOBBY);
        this.pl = plugin;

        if(c.getString("spawn.world") != null) {
            double x = c.getDouble("spawn.x");
            double y = c.getDouble("spawn.y");
            double z = c.getDouble("spawn.z");
            float pitch = (float) c.getDouble("spawn.pitch");
            float yaw = (float) c.getDouble("spawn.yaw");
            World world = Bukkit.getWorld(c.getString("spawn.world"));

            spawn = new Location(world, x, y, z, yaw, pitch);
        }
    }

    public Location GetSpawn() {
        FileConfiguration mainC = ConfigUtil.GetConfig(ConfigUtil.ConfigType.SETTINGS);
        if(mainC.getBoolean("lobby-settings.use-plugin-spawn"))
            if(spawn != null) return spawn;

        Location location = Bukkit.getWorld(mainC.getString("lobby-settings.spawn-world")).getSpawnLocation();

        return location != null ? location : Bukkit.getWorlds().get(0).getSpawnLocation();
    }

    public void TeleportPlayer(WarriorUser user) {
        user.teleport(GetSpawn());
        SetInventory(user.bukkit());

        user.inLobby(true);
    }

    private void SetInventory(Player player) {
        // TODO: MOVE LOBBY ITEMS TO JSON FILES (./itemdata/(itemname).json)
        ItemStack arenaSelector = new CustomItem(Material.IRON_SWORD, 1, "§3§lArena Selector §r» §8(right click)")
                .hideFlags(true).setLore("§7Opens the arena GUI").build();
        ItemStack kitPreview = new CustomItem(Material.CHEST, 1, "§6§lClass Preview §r» §8(right click)")
                .hideFlags(true).setLore("§7Opens the kit preview GUI").build();

        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.getInventory().clear();
        player.getInventory().setItem(1, arenaSelector);
        player.getInventory().setItem(4, kitPreview);

    }

    public void SetSpawn(Location loc) {
        spawn = loc;

        c.set("spawn.x", loc.getX());
        c.set("spawn.y", loc.getY());
        c.set("spawn.z", loc.getZ());
        c.set("spawn.pitch", loc.getPitch());
        c.set("spawn.yaw", loc.getYaw());
        c.set("spawn.world", loc.getWorld().getName());

        try {
            c.save(ConfigUtil.ConfigFile(ConfigUtil.ConfigType.LOBBY));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
