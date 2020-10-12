package com.kokumaji.Warrior.Utils;

import com.kokumaji.Warrior.Game.Managers.UserManager;
import com.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.HibiscusAPI.api.translation.ReplaceExpansion;
import org.bukkit.OfflinePlayer;
public class Placeholders extends ReplaceExpansion {

    public Placeholders(String owning) {
        super(owning);
    }

    @Override
    public String replace(OfflinePlayer offlinePlayer, String s) {
        String[] arguments = s.split("_");
        String output = s;

        //TODO: add configurable main/accent color placeholder

        WarriorUser u = UserManager.GetPlayer(offlinePlayer.getUniqueId());
        switch (arguments[0]) {
            case "player":
                output = offlinePlayer.getName();
                break;
            case "kills":
                output = String.valueOf(u.getKills());
                break;
            case "deaths":
                output = String.valueOf(u.getDeaths());
                break;
            case "kdr":
                output = String.valueOf(u.getKDR());
                break;
            case "ping":
                output = colorizedPing(u.getPing());
                break;
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
