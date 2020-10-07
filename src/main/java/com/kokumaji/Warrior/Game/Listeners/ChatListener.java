package com.kokumaji.Warrior.Game.Listeners;

import com.kokumaji.Warrior.Utils.ChatMessage;
import com.kokumaji.Warrior.Warrior;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.isCancelled()) return;

        FileConfiguration c = Warrior.GetPlugin().getConfig();
        if(c.getBoolean("use-modules.use-chat-manager")) {
            String content = e.getMessage();
            OfflinePlayer p = e.getPlayer();

            e.setCancelled(true);

            ChatMessage msg = new ChatMessage(p, content);
            for(Player pl : Bukkit.getOnlinePlayers()) {
                pl.spigot().sendMessage(msg.getChatMessage());
            }

            System.out.println(msg.getChatMessage().toPlainText());
        }

    }
}
