package com.kokumaji.Warrior.Utils;

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
            switch (arguments[1]) {
                case "uppercase":
                    output = output.toUpperCase();
            }
        }

        return output;
    }
}
