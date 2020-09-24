package com.kokumaji.StickyPvP.Utils;

import net.md_5.bungee.api.chat.TextComponent;

import com.kokumaji.StickyPvP.Game.Objects.User;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.w3c.dom.Text;

public class MessageUtil {
    private final static int CENTER_CHAT_PX = 154;
    private final static int MAX_CHAT_PX = 250;

    public static String ApplyColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void CenterMessage(Player player, TextComponent... messageArray) {
        for (TextComponent message : messageArray) {
            if (message == null || message.equals("")) player.sendMessage("");
            message.setText(ChatColor.translateAlternateColorCodes('&', message.getText()));

            int messagePxSize = 0;
            boolean previousCode = false;
            boolean isBold = false;

            for (char c : message.getText().toCharArray()) {
                if (c == '§') {
                    previousCode = true;
                    continue;
                } else if (previousCode == true) {
                    previousCode = false;
                    if (c == 'l' || c == 'L') {
                        isBold = true;
                        continue;
                    } else isBold = false;
                } else {
                    DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                    messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                    messagePxSize++;
                }
            }

            int halvedMessageSize = messagePxSize / 2;
            int toCompensate = CENTER_CHAT_PX - halvedMessageSize;
            int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
            int compensated = 0;
            StringBuilder sb = new StringBuilder();
            while (compensated < toCompensate) {
                sb.append(" ");
                compensated += spaceLength;
            }

            String content = message.getText();
            message.setText(sb.toString() + content);
            player.spigot().sendMessage(message);
        }
    }

    public static void CenterMessage(Player player, String... messageArray) {
        for (String message : messageArray) {
            if (message == null || message.equals("")) player.sendMessage("");
            message = ChatColor.translateAlternateColorCodes('&', message);

            int messagePxSize = 0;
            boolean previousCode = false;
            boolean isBold = false;

            for (char c : message.toCharArray()) {
                if (c == '§') {
                    previousCode = true;
                    continue;
                } else if (previousCode == true) {
                    previousCode = false;
                    if (c == 'l' || c == 'L') {
                        isBold = true;
                        continue;
                    } else isBold = false;
                } else {
                    DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                    messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                    messagePxSize++;
                }
            }

            int halvedMessageSize = messagePxSize / 2;
            int toCompensate = CENTER_CHAT_PX - halvedMessageSize;
            int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
            int compensated = 0;
            StringBuilder sb = new StringBuilder();
            while (compensated < toCompensate) {
                sb.append(" ");
                compensated += spaceLength;
            }
            player.sendMessage(sb.toString() + message);
        }
    }

    public static void CenterMessage(Player player, Object... messageArray) {
        for(Object o : messageArray) {
            if(o instanceof String) CenterMessage(player, (String) o);
            if(o instanceof TextComponent) CenterMessage(player, (TextComponent) o);
        }
    }

	public static void UnknownSubCommand(User u) {
        CenterMessage(u.Bukkit(), " ", TranslationsUtil.HL, " ", "&7Unknown sub-command.", "&7Type &b/kitpvp commands &7for help.", " ", TranslationsUtil.HL, " ");
        u.Bukkit().playSound(u.Bukkit().getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1.5f);
	}

	public static void UnknownArgument(User u, String string) {
        CenterMessage(u.Bukkit(), " ", TranslationsUtil.HL, " ", "&b" + string + " &7is not a valid argument.", "&7Type &b/kitpvp commands &7for help.", " ", TranslationsUtil.HL, " ");
        u.Bukkit().playSound(u.Bukkit().getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1.5f);
	}

	public static void MissingArguments(User u) {
        CenterMessage(u.Bukkit(), " ", TranslationsUtil.HL, " ", "&7Missing Arguments.", "&7Type &b/kitpvp commands &7for help.", " ", TranslationsUtil.HL, " ");
        u.Bukkit().playSound(u.Bukkit().getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1.5f);
	}
}
