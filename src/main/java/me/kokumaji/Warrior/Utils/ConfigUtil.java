package me.kokumaji.Warrior.Utils;

import me.kokumaji.Warrior.Warrior;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;

public class ConfigUtil {
    private static final Plugin self = Warrior.getPlugin();
    private static String prefix;
    private static FileConfiguration fc;
    private static FileConfiguration fcA;
    private static FileConfiguration fcL;
    private static File configFile;

    public enum ConfigType {
        LOBBY,
        ARENA,
        SETTINGS;
    }

    public static String GenerateID(Collection context) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        if(context.contains(saltStr)) GenerateID(context);
        return saltStr;
    }

    public static String GenerateID(Collection context, int length) {
        if(length > 10) return null;
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        if(context.contains(saltStr)) GenerateID(context, length);
        return saltStr;
    }

    public static String GenerateID(int length) {
        if(length > 10) return null;
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();

        return saltStr;
    }

    public static void CopyConfig(boolean Overwrite) {
        File file = new File(self.getDataFolder() + "/config.yml");
        boolean copied = false;
        if(Overwrite) {
            self.saveResource("config.yml", true);
            copied = true;
        } else {
            if(configFile == null && !file.exists()) {
                self.saveResource("config.yml", false);
                copied = true;
            }
        }

        configFile = ConfigFile(ConfigType.SETTINGS);
        assert configFile != null;
        fc = YamlConfiguration.loadConfiguration(configFile);

        LoadEditable();

        prefix = MessageUtil.ApplyColor(fc.getString("general-settings.plugin-prefix"));

        if(copied)
            Warrior.getApiProvider().getLogger().info(InternalMessages.CONFIG_COPIED);

    }

    private static void LoadEditable() {
        File fA = new File(self.getDataFolder() + "/arenas.yml");
        File fL = new File(self.getDataFolder() + "/lobby.yml");

        if(fA.exists())
            fcA = YamlConfiguration.loadConfiguration(fA);

        if(fL.exists())
            fcL = YamlConfiguration.loadConfiguration(fL);
    }

    public static String GetPrefix() {
        if(prefix == null) return "§3§lWarrior §8»§r ";
        if(prefix.endsWith(" ")) return prefix;
        return prefix + " ";
    }

    public static File ConfigFile(ConfigType ConfigType) {
        switch(ConfigType) {
            case SETTINGS:
                return new File(self.getDataFolder() + "/config.yml");
            case ARENA:
                return new File(self.getDataFolder() + "/arena.yml");
            case LOBBY:
                return new File(self.getDataFolder() + "/lobby.yml");
        }

        return null;
    }

    public static FileConfiguration GetConfig(ConfigType configType) {
        switch (configType) {
            case SETTINGS:
                return fc;
            case ARENA:
                return CheckConfig(ConfigType.ARENA);
            case LOBBY:
                return CheckConfig(ConfigType.LOBBY);
        }

        return null;
    }

    private static FileConfiguration CheckConfig(ConfigType configType) {
        File f = null;

        if(configType == ConfigType.ARENA)
            f = new File(self.getDataFolder() + "/arenas.yml");

        if(configType == ConfigType.LOBBY)
            f = new File(self.getDataFolder() + "/lobby.yml");

        try {
            if(!f.exists()) {
                f.createNewFile();
                if(configType.equals(ConfigType.ARENA)) return fcA = YamlConfiguration.loadConfiguration(f);
                if(configType.equals(ConfigType.LOBBY)) return fcL = YamlConfiguration.loadConfiguration(f);
            }

            if(configType.equals(ConfigType.ARENA)) return fcA;
            if(configType.equals(ConfigType.LOBBY)) return fcL;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void ReloadConfig(ConfigType confType) {
        switch(confType) {
            case SETTINGS:
                fc = YamlConfiguration.loadConfiguration(ConfigFile(confType));
            case ARENA:
                return;
        }
    }

    public static LanguageCode GetDefaultLang() {
        LanguageCode l = LanguageCode.fromString(fc.getString("general-settings.default-language"));
        return l != null ? l : LanguageCode.en_US;
    }
}
