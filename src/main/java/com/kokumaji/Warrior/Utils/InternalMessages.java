package com.kokumaji.Warrior.Utils;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public enum InternalMessages {

    CONFIG_COPIED("§7Generating new §bconfig file§7...", Level.INFO),
    STORAGE_TYPE_NOT_IMPLEMENTED("§7The storage type '§b{0}§7' is not yet implemented. Plugin is running in §bDemo Mode§7.", Level.WARNING),
    STORAGE_TYPE_INVALID("§7The storage type '{0}' is not valid. Plugin is running in §bDemo Mode§7.", Level.WARNING),
    SQL_DISABLED("§bSQL is not enabled! §7Plugin is running in §bDemo Mode§7.", Level.INFO),
    NMS_SUPPORTED("(Debug) Version {0} is supported.", Level.INFO),
    NMS_UNSUPPORTED("§7Warrior does not support version §b{0}", Level.INFO),
    NMS_CLASS_NOT_FOUND("§7Could not find class §c{0}§7. Contact the plugin author if you believe this is an error.", Level.SEVERE),
    TRANSLATIONS_FOLDER_MISSING("§7Generating new §blanguage folder§7...", Level.INFO),
    TRANSLATIONS_FOLDER_CHECK("§7Checking for missing §blanguage files§7...", Level.INFO),
    SQL_CONNECT_ERROR("§cCould not connect to SQL database. Reason: {0}", Level.SEVERE),
    SQL_CONNECT_SUCCESS("§aSuccessfully connected to the SQL database.", Level.INFO),

    // DEBUG MESSAGES
    CACHE_PLAYER_ADDED("§7Adding {0} ({1}) to cache.", Level.INFO),
    CACHE_PLAYER_REMOVED("§7Removing {0} ({1}) from cache.", Level.INFO), 
    TELEPORT_LOCATION_NULL("§7Plugin attempted to teleport user {0}, but location was null.", Level.WARNING), 
    ARENA_REGISTERED_SUCCESS("§7Registered new arena §b{0}", Level.INFO), 
    KIT_NOT_FOUND("§7Player §b{0} §7attempted to select a kit, but the result was null.", Level.WARNING);

    String msg;
    Level l;
    InternalMessages(String s, Level level) {
        this.l = level;
        this.msg = ConfigUtil.GetPrefix() + s;
    }

    public void Log() {
        Bukkit.getLogger().log(this.l, this.msg);
    }

    public void Log(String... args) {
        Map<String, String> values = new HashMap<String, String>();
        int i = 0;
        for(String s : args) {
            values.put(String.valueOf(i), s);
            i++;
        }
        StrSubstitutor sub = new StrSubstitutor(values, "{", "}");
        String result = sub.replace(this.msg);

        Bukkit.getLogger().log(this.l, result);
    }
}
