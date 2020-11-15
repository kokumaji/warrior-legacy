package me.kokumaji.Warrior.Utils;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class InternalMessages {

    /**
     * GENERAL SYSTEM MESSAGES
     */
    public static final String CONFIG_COPIED = "§7Generating new §bConfig File§7...";
    
    public static final String STORAGE_TYPE_NOT_IMPLEMENTED = "§7The storage type '§b{0}§7' is not yet implemented. Plugin is running in §bDemo Mode§7.";

    public static final String STORAGE_TYPE_INVALID = "§7The storage type '{0}' is not valid. Plugin is running in §bDemo Mode§7.";

    public static final String SQL_DISABLED = "§bSQL is not enabled! §7Plugin is running in §bDemo Mode§7.";

    /**
     * DEBUG MESSAGES
     */
    public static final String CACHE_PLAYER_ADDED = "§7Adding {0} ({1}) to cache.";
    public static final String CACHE_PLAYER_REMOVED = "§7Removing {0} ({1}) from cache.";

    public static final String ARENA_REGISTERED_SUCCESS = "§7Registered new arena §b{0}";
    public static final String KIT_NOT_FOUND = "§7Player §b{0} §7attempted to select a kit, but the result was null.";
    public static final String SQL_GENERAL_ERROR = "§7Encountered an error while trying to execute an SQL statement.";

    public static String format(String message, String... args) {
        Map<String, String> values = new HashMap<String, String>();
        int i = 0;
        for(String s : args) {
            values.put(String.valueOf(i), s);
            i++;
        }
        StrSubstitutor sub = new StrSubstitutor(values, "{", "}");
        String result = sub.replace(message);

        return result;
    }
}
