package com.kokumaji.StickyPvP.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import com.kokumaji.StickyPvP.StickyPvP;
import com.kokumaji.StickyPvP.Game.Managers.UserManager;
import com.kokumaji.StickyPvP.Game.Objects.User;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationsUtil {

    // FIXME: if placeholhderapi not found: return without papi!!

    private static Plugin self = StickyPvP.getPlugin(StickyPvP.class);
    private static FileConfiguration plConf;
    private static LanguageCode defaultLang = LanguageCode.en_US;

    public static final String HL = "&m                                                &c";

    public static String HL(int length) {
        String hl = null;
        for(int i = 0; i < length; i++) {
            hl = hl + " ";
        }

        return "&m" + hl + "&r";
    }

    public TranslationsUtil(FileConfiguration conf) {
        File langFolder = new File(self.getDataFolder(), "lang");
        plConf = conf;

        if(!langFolder.exists()) {
            langFolder.mkdirs();
            InternalMessages.TRANSLATIONS_FOLDER_MISSING.Log();

            self.saveResource("lang/messages.en_US.yml", false);
            self.saveResource("lang/messages.de_DE.yml", false);

        } else {
            InternalMessages.TRANSLATIONS_FOLDER_CHECK.Log();
            File enLangFile = new File(self.getDataFolder(), "lang/messages.en_US.yml");
            File deLangFile = new File(self.getDataFolder(), "lang/messages.de_DE.yml");
            if(!enLangFile.exists()) {
                self.saveResource("lang/messages.en_US.yml", false);
            }
            if(!deLangFile.exists()) {
                self.saveResource("lang/messages.de_DE.yml", false);
            }

        }

        // FIXME: LanguageCode#fromString() returns null?
        // defaultLang = LanguageCode.fromString(plConf.getString("general-settings.default-language"));
    }

    /**
     *
     * @param language ISO 639-1 language code (https://www.andiamo.co.uk/resources/iso-language-codes/)
     * @return Language file (or null if file does not exist)
     */
    public static YamlConfiguration GetLanguageFile(LanguageCode language) {
        File f = new File(self.getDataFolder() + "/lang/messages." + language.toString() + ".yml");
        return YamlConfiguration.loadConfiguration(f);
    }

    public static String Translate(Player player, String stringPath, boolean addPluginPrefix, Map<String, String> values) {
        User User = UserManager.GetPlayer(player.getUniqueId());
        LanguageCode lang = User != null ? User.GetLang() : defaultLang;
        YamlConfiguration config = GetLanguageFile(lang);
        if(config.get(stringPath) != null) {
            StrSubstitutor sub = new StrSubstitutor(values, "{", "}");
            String result = sub.replace(config.getString(stringPath));
            if(addPluginPrefix) {
                result = ConfigUtil.GetPrefix() + " " + result;
            }
            if(((StickyPvP)self).UsePAPI()) {
                result = PlaceholderAPI.setPlaceholders(player, result);
            }
            return MessageUtil.ApplyColor(result);
        }

        return "§cError in language file " + lang.toString() + "! String " + stringPath + " does not exist.";
    }

    public static String Translate(Player player, String stringPath, boolean addPluginPrefix) {
        User User = UserManager.GetPlayer(player.getUniqueId());
        LanguageCode lang = User != null ? User.GetLang() : defaultLang;
        YamlConfiguration config = GetLanguageFile(lang);
        if(config.get(stringPath) != null) {
            String result = config.getString(stringPath);
            if(addPluginPrefix)
                result = ConfigUtil.GetPrefix() + " " + result;

            if(((StickyPvP)self).UsePAPI())
                result = PlaceholderAPI.setPlaceholders(player, result);

            return MessageUtil.ApplyColor(result);
        }

        return "§cError in language file " + lang.toString() + "! String " + stringPath + " does not exist.";
    }


    public static String Translate(String stringPath, boolean addPluginPrefix) {
        YamlConfiguration config = GetLanguageFile(defaultLang);
        if(config.get(stringPath) != null) {
            String result = config.getString(stringPath);
            if(addPluginPrefix)
                result = ConfigUtil.GetPrefix() + " " + result;

            return MessageUtil.ApplyColor(result);
        }

        return "§cError in language file " + LanguageCode.en_US.toString() + "! String " + stringPath + " does not exist.";
    }

    public static String ReadableLocation(Location location, boolean showVar, boolean showWorld) {
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

    public static boolean ContainsSpecialCharacters(String s) {
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        boolean b = m.find();

        return b;
    }

}
