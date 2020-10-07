package com.kokumaji.Warrior.Utils;

import com.kokumaji.Warrior.Game.Managers.UserManager;
import com.kokumaji.Warrior.Game.Objects.User;
import me.kokumaji.HibiscusAPI.api.translation.ReplaceExpansion;
import org.bukkit.OfflinePlayer;
public class Placeholders extends ReplaceExpansion {

    public Placeholders(String owning) {
        super(owning);
    }

    @Override
    public String replace(OfflinePlayer offlinePlayer, String s) {
        String[] arguments = s.split(":");
        String output = s;

        switch (arguments[0]) {
            case "player":
                output = offlinePlayer.getName();
        }
        if(arguments.length > 1) {
            User u = UserManager.GetPlayer(offlinePlayer.getUniqueId());

            switch (arguments[1]) {
                case "kills":
                    output = String.valueOf(u.GetKills());
                    break;
                case "deaths":
                    output = String.valueOf(u.GetDeaths());
                    break;
                case "kdr":
                    output = String.valueOf(u.GetKDR());
                    break;
                case "ping":
                    try {
                        output = colorizedPing(u.GetPing());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        return output;
    }

    public String colorizedPing(int ping) {
        if(ping < 100) {
            return "§a" + ping + "ms";
        } else if(ping < 400) {
            return "§e" + ping + "ms";
        }

        return "§c" + ping + "ms";

    }
}
