package com.kokumaji.Warrior.Game.Listeners;

import com.kokumaji.Warrior.Utils.ChatMessage;
import com.kokumaji.Warrior.Warrior;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
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


            Translator t = Warrior.getTranslator();
            ChatMessage msg = new ChatMessage(p, t.parsePlaceholder(e.getPlayer(), content));

            for(Player pl : Bukkit.getOnlinePlayers()) {
                pl.spigot().sendMessage(msg.getChatMessage());
            }
            
            System.out.println(t.parsePlaceholder(e.getPlayer(), msg.getChatMessage().toPlainText()));
        }

    }
}
