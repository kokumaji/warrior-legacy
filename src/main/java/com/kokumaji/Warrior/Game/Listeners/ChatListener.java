package com.kokumaji.Warrior.Game.Listeners;

import com.kokumaji.Warrior.Utils.MessageUtil;
import com.kokumaji.Warrior.Warrior;
import me.kokumaji.HibiscusAPI.api.translation.ChatMessage;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import net.md_5.bungee.api.chat.TextComponent;
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

            long startTime = System.nanoTime();
            ChatMessage msg = new ChatMessage(t.parsePlaceholder(e.getPlayer(), MessageUtil.ApplyColor(content)))
                    .applyURLs(true);
            long endTime = System.nanoTime();
            double execTime = (double)(endTime - startTime) / 1000000;
            System.out.println("§eWARRIOR | DEBUG §7» Chat message builder took §6" + execTime + "ms §7to complete. §r(player=Player, content=$msg)");

            ChatMessage chat = new ChatMessage(p.getName() + " » ").appendMessage(msg);

            for(Player pl : Bukkit.getOnlinePlayers()) {
                pl.spigot().sendMessage(chat.getComponent());
            }
            
            System.out.println(chat.getRawContent());
        }

    }
}
