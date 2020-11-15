package me.kokumaji.Warrior.Utils;

import me.kokumaji.Warrior.Game.Managers.ArenaManager;
import me.kokumaji.Warrior.Game.Objects.Arena;
import me.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.HibiscusAPI.api.translation.ReplaceExpansion;
import me.kokumaji.Warrior.Warrior;
import org.bukkit.Bukkit;
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

        WarriorUser u = Warrior.getUserCache().get(offlinePlayer.getUniqueId());
        switch (arguments[0]) {
            case "online":
                return String.valueOf(Bukkit.getOnlinePlayers().size());
            case "maxplayers":
                return String.valueOf(Bukkit.getServer().getMaxPlayers());
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
            case "arena":
                if(arguments.length > 1) {
                    Arena a = null;
                    if(MessageUtil.isInteger(arguments[1])) {
                        a = ArenaManager.GetArena(Integer.parseInt(arguments[1]));
                    } else {
                        a = ArenaManager.GetArena(arguments[1]);
                    }

                    if(a == null) return null;

                    if(arguments.length == 3) {
                        switch(arguments[2]) {
                            case "spawn":
                                return MessageUtil.readableLocation(a.GetSpawn(), true, false);
                            case "name":
                                return a.GetName();
                            case "id":
                                return String.valueOf(a.GetId());
                            case "maxplayers":
                                return String.valueOf(a.GetMaxPlayers());
                        }
                    }

                    return a.GetName();
                }
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
