package me.kokumaji.Warrior.Game.Listeners;

import me.kokumaji.Warrior.Utils.MessageUtil;
import me.kokumaji.Warrior.Warrior;
import me.clip.placeholderapi.PlaceholderAPI;
import me.kokumaji.HibiscusAPI.api.HibiscusDebugger;
import me.kokumaji.HibiscusAPI.api.translation.ChatMessage;
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

        FileConfiguration c = Warrior.getPlugin().getConfig();
        if(c.getBoolean("use-modules.use-chat-manager")) {
            String content = e.getMessage();
            OfflinePlayer p = e.getPlayer();

            e.setCancelled(true);


            Translator t = Warrior.getTranslator();
            String chatFormat = c.getString("chat-settings.chat-format");

            HibiscusDebugger debugger = null;
            if(Warrior.getApiProvider().debugMode)
                debugger = new HibiscusDebugger();

            ChatMessage msg = new ChatMessage(t.parsePlaceholder(e.getPlayer(), MessageUtil.ApplyColor(content)))
                    .applyURLs(true);
            ChatMessage chat = new ChatMessage(PlaceholderAPI.setPlaceholders(p, chatFormat)).appendMessage(msg);

            for(Player pl : Bukkit.getOnlinePlayers()) {
                pl.spigot().sendMessage(chat.getComponent());
            }

            System.out.println(chat.getRawContent());

            if(debugger != null)
                debugger.end();
        }

    }
}
