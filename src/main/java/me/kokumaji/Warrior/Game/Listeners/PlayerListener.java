package me.kokumaji.Warrior.Game.Listeners;

import me.kokumaji.HibiscusAPI.api.util.TimeUtil;
import me.kokumaji.Warrior.Game.Managers.LobbyManager;
import me.kokumaji.Warrior.Game.Managers.MOTDManager;
import me.kokumaji.Warrior.Game.Objects.UserStats;
import me.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.Warrior.Utils.DatabaseUtil;
import me.kokumaji.Warrior.Utils.InternalMessages;
import me.kokumaji.Warrior.Warrior;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import me.kokumaji.Warrior.Utils.ConfigUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import java.util.HashMap;

public class PlayerListener implements Listener {

    private Warrior self = (Warrior) Warrior.getPlugin();

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(!Warrior.getUserCache().contains(p.getUniqueId())) {
            DatabaseUtil dbUtil = (DatabaseUtil)self.getUtil("sql");
            dbUtil.execute("INSERT IGNORE INTO $table_player_stats VALUES ('" + p.getUniqueId() + "', '" + p.getName() + "',0, 0, 0, 'en_US', " + TimeUtil.now() + ")");

            UserStats stats = dbUtil.getUserData(p.getUniqueId());
            WarriorUser user = new WarriorUser(p, stats);
            Warrior.getUserCache().add(user);

            FileConfiguration c = ConfigUtil.GetConfig(ConfigUtil.ConfigType.SETTINGS);

            if(c.getBoolean("general-settings.debug")) {
                String formatted = InternalMessages.format(InternalMessages.CACHE_PLAYER_ADDED,
                                        p.getName(), String.valueOf(p.getUniqueId()));

                Warrior.getPluginLogger().debug(formatted);
            }


            if(c.getBoolean("lobby-settings.teleport-on-join")) {
                Sound sound = null;

                if(!c.getString("lobby-settings.play-sound").toLowerCase().equals("none"))
                    sound = Sound.valueOf(c.getString("lobby-settings.play-sound").toUpperCase());

                LobbyManager lm = (LobbyManager) self.getManager("lobby");
                lm.TeleportPlayer(user);

                user.playSound(sound);
            }

            Translator t = Warrior.getTranslator();

            if(c.getBoolean("chat-settings.handle-first-join")) {
                Location loc = user.bukkit().getLocation();
                Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                fwm.setPower(1);
                fwm.addEffect(FireworkEffect.builder().withColor(Color.AQUA).flicker(true).with(FireworkEffect.Type.BURST).build());
                fw.setFireworkMeta(fwm);

                e.setJoinMessage(t.Translate(p, "general-messages.first-join", true));
                return;
            }

            if(c.getBoolean("chat-settings.announce-join")) {
                e.setJoinMessage(t.Translate(p, "general-messages.join-message", true));
            }

            if(c.getBoolean("chat-settings.send-motd")) {
                MOTDManager mm = (MOTDManager) self.getManager("motd");
                mm.SendMOTD(user);
            }
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(Warrior.getUserCache().contains(p.getUniqueId())) {
            WarriorUser user = Warrior.getUserCache().get(p.getUniqueId());
            user.saveData(self);
            Warrior.getUserCache().remove(user);

            FileConfiguration c = ConfigUtil.GetConfig(ConfigUtil.ConfigType.SETTINGS);

            if(c.getBoolean("general-settings.debug")) {
                String formatted = InternalMessages.format(InternalMessages.CACHE_PLAYER_REMOVED,
                        p.getName(), String.valueOf(p.getUniqueId()));

                Warrior.getPluginLogger().debug(formatted);
            }

            if(c.getBoolean("chat-settings.announce-quit")) {
                e.setQuitMessage(null);
                Translator t = Warrior.getTranslator();
                for(WarriorUser u : Warrior.getUserCache().getValues()) {
                    u.sendMessage(t.Translate(p, "general-messages.quit-message", true, new HashMap<String, String>() {
                        {
                            put("Player", p.getName());
                            put("Online", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
                            put("MaxPlayers", String.valueOf(Bukkit.getServer().getMaxPlayers()));
                        }
                    }));
                }
            }
        }
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e) {
        WarriorUser u = Warrior.getUserCache().get(e.getPlayer().getUniqueId());
        if(u.isInLobby()) {
            ItemStack is = e.getItem();

            if(is == null) return;

            if(is.getType().equals(Material.IRON_SWORD)) {
                Bukkit.dispatchCommand(u.bukkit(), "arena");
            }
        }
    }
}
