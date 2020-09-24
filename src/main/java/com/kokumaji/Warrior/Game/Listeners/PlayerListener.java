package com.kokumaji.Warrior.Game.Listeners;

import com.kokumaji.Warrior.Game.Managers.LobbyManager;
import com.kokumaji.Warrior.Game.Managers.MOTDManager;
import com.kokumaji.Warrior.Game.Managers.UserManager;
import com.kokumaji.Warrior.Game.Objects.User;
import com.kokumaji.Warrior.Game.Objects.UserStats;
import com.kokumaji.Warrior.Warrior;
import com.kokumaji.Warrior.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerListener implements Listener {

    private Warrior self = (Warrior) Warrior.GetPlugin();

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(!UserManager.IsCached(p.getUniqueId())) {
            DatabaseUtil dbUtil = (DatabaseUtil)self.GetUtil("sql");
            dbUtil.Execute("INSERT IGNORE INTO $table_player_stats VALUES ('" + p.getUniqueId() + "', 0, 0, 0, 'en_US')");

            UserStats stats = dbUtil.GetUserData(p.getUniqueId());
            User user = new User(p, stats);
            UserManager.AddPlayer(user);

            FileConfiguration c = ConfigUtil.GetConfig(ConfigUtil.ConfigType.SETTINGS);

            if(c.getBoolean("general-settings.debug"))
                InternalMessages.CACHE_PLAYER_ADDED.Log(p.getName(), String.valueOf(p.getUniqueId()));

            if(c.getBoolean("lobby-settings.teleport-on-join")) {
                Sound sound = null;

                if(!c.getString("lobby-settings.play-sound").toLowerCase().equals("none"))
                    sound = Sound.valueOf(c.getString("lobby-settings.play-sound").toUpperCase());

                LobbyManager lm = (LobbyManager) self.GetManager("lobby");
                lm.TeleportPlayer(user);

                user.PlaySound(sound);
            }

            if(c.getBoolean("chat-settings.announce-join")) {
                e.setJoinMessage(null);
                TranslationsUtil t = (TranslationsUtil) self.GetUtil("trans");
                for(User u : UserManager.GetPlayers()) {
                    u.SendMessage(t.Translate(p, "general-messages.join-message", true, new HashMap<String, String>() {
                        {
                            put("Player", p.getName());
                            put("Online", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
                            put("MaxPlayers", String.valueOf(Bukkit.getServer().getMaxPlayers()));
                        }
                    }));
                }
            }

            if(c.getBoolean("chat-settings.send-motd")) {
                MOTDManager mm = (MOTDManager) self.GetManager("motd");
                mm.SendMOTD(user);
            }
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(UserManager.IsCached(p.getUniqueId())) {
            User user = UserManager.GetPlayer(p.getUniqueId());
            user.SaveData(self);
            UserManager.RemovePlayer(p.getUniqueId());

            FileConfiguration c = ConfigUtil.GetConfig(ConfigUtil.ConfigType.SETTINGS);

            if(c.getBoolean("general-settings.debug"))
                InternalMessages.CACHE_PLAYER_REMOVED.Log(p.getName(), String.valueOf(p.getUniqueId()));

            if(c.getBoolean("chat-settings.announce-quit")) {
                e.setQuitMessage(null);
                TranslationsUtil t = (TranslationsUtil) self.GetUtil("trans");
                for(User u : UserManager.GetPlayers()) {
                    u.SendMessage(t.Translate(p, "general-messages.quit-message", true, new HashMap<String, String>() {
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
}
