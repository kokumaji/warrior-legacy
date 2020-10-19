package com.kokumaji.Warrior.Utils;

import com.kokumaji.Warrior.Game.Objects.WarriorUser;
import net.md_5.bungee.api.chat.TextComponent;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static final String HL = "&m                                                &c";

    public static String HL(int length) {
        String hl = null;
        for(int i = 0; i < length; i++) {
            hl = hl + " ";
        }

        return "&m" + hl + "&r";
    }
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

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static String readableLocation(Location location, boolean showVar, boolean showWorld) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        String output;

        if(showVar) {
            output = String.format("X%d Y%2d Z%3d", x, y, z);
        } else {
            output = String.format("%d %2d %3d", x, y, z);
        }

        if(showWorld) {
            output = String.format(output + ", World %s", location.getWorld().getName());
        }

        return output;

    }

}
