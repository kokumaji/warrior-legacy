/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package me.kokumaji.Warrior;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.kokumaji.HibiscusAPI.api.HibiscusDebugger;
import me.kokumaji.HibiscusAPI.api.storage.cache.Cache;
import me.kokumaji.HibiscusAPI.api.util.TimeUtil;
import me.kokumaji.Warrior.Commands.Game.ArenaCommand;
import me.kokumaji.Warrior.Commands.Game.KitCommand;
import me.kokumaji.Warrior.Commands.General.MainCommand;
import me.kokumaji.Warrior.Game.Listeners.ChatListener;
import me.kokumaji.Warrior.Game.Listeners.PlayerListener;
import me.kokumaji.Warrior.Game.Objects.GUIs.GUIHandler;
import me.kokumaji.Warrior.Game.Objects.UserStats;
import me.kokumaji.Warrior.Game.Objects.WarriorUser;

import lombok.Getter;
import me.kokumaji.HibiscusAPI.HibiscusAPI;
import me.kokumaji.HibiscusAPI.HibiscusLogger;
import me.kokumaji.HibiscusAPI.api.storage.DatabaseConnection;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import me.kokumaji.Warrior.Game.Managers.*;
import me.kokumaji.Warrior.Utils.ConfigUtil;
import me.kokumaji.Warrior.Utils.DatabaseUtil;
import me.kokumaji.Warrior.Utils.InternalMessages;
import me.kokumaji.Warrior.Utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Warrior extends JavaPlugin {

    public static ExecutorService pool = Executors.newFixedThreadPool(3);
    private static List<Command> cmds = new ArrayList<Command>();
    private CommandMap cMap;

    static Translator translator;
    DatabaseUtil sqlUtil;
    LobbyManager lobbyManager;
    MOTDManager motdManager;
    private FileConfiguration c;
    private boolean usePlaceholderAPI;

    @Getter
    public static HibiscusLogger pluginLogger;

    @Getter
    public static Cache<WarriorUser> userCache = new Cache<>();

    static HibiscusAPI apiProvider;

    @Override
    public void onEnable() {

        ConfigUtil.CopyConfig(false);
        c = ConfigUtil.GetConfig(ConfigUtil.ConfigType.SETTINGS);

        apiProvider = new HibiscusAPI(this);
        apiProvider.debugMode = c.getBoolean("general-settings.debug");
        pluginLogger = apiProvider.getLogger();

        HibiscusDebugger debugger = null;
        if(apiProvider.debugMode) {
            debugger = new HibiscusDebugger("Warrior#<init>()");
        }

        if(!checkSupported()) {
            sendWarnMessage();
        }

        if(c.getBoolean("use-modules.placeholderapi-support")) {
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new PlaceholderExpansion(this).register();
                usePlaceholderAPI = true;
            } else {
                pluginLogger.warn("PlaceholderAPI not found. Skipping...");
            }
        }

        try {
            translator = new Translator(this, c, true);
            Translator.registerReplacer(new Placeholders("warrior"));
        } catch (IOException e) {
            pluginLogger.error(e);
        }

        lobbyManager = new LobbyManager(this);

        this.getCommandMap();
        
        cmds.add(new MainCommand("warrior", this));
        cmds.add(new ArenaCommand("arena", this));
        cmds.add(new KitCommand("kit", this));
        cMap.registerAll(this.getName().toLowerCase(), cmds);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), this);

        ArenaManager.LoadArenas();
        KitManager.RegisterKits();

        File npcFolder = new File(getDataFolder(), "npcdata");
        if(!npcFolder.exists())
            npcFolder.mkdirs();

        if(c.getBoolean("chat-settings.send-motd")) {
            motdManager = new MOTDManager(c, this);
        }

        GUIHandler.RegisterGUIs();

        String dataType = c.getString("data-storage.storage-method");
        if(dataType.toLowerCase().equalsIgnoreCase("mysql")) {

            DatabaseConnection.ConnectionDetails details = new DatabaseConnection.ConnectionDetails(
                    c.getString("data-storage.mysql-settings.host"),
                    c.getInt("data-storage.mysql-settings.port"),
                    c.getString("data-storage.mysql-settings.username"),
                    c.getString("data-storage.mysql-settings.password"),
                    c.getString("data-storage.mysql-settings.database"),
                    c.getString("data-storage.mysql-settings.table-prefix"),
                    false
            );
            sqlUtil = new DatabaseUtil(c, details);
            pluginLogger.info("SQL Database connection successful!");
            sqlUtil.makeTables();
        } else if(dataType.toLowerCase().equalsIgnoreCase("h2") || dataType.toLowerCase().equalsIgnoreCase("flatfile")) {
            String formatted = InternalMessages.format(InternalMessages.STORAGE_TYPE_NOT_IMPLEMENTED, dataType);
            pluginLogger.info(formatted);
        } else {
            String formatted = InternalMessages.format(InternalMessages.STORAGE_TYPE_INVALID, dataType);
            pluginLogger.info(formatted);
        }

        for(Player p : Bukkit.getOnlinePlayers()) {
            sqlUtil.execute("INSERT IGNORE INTO $table_player_stats VALUES ('" + p.getUniqueId() + "', '" + p.getName() + "', 0, 0, 0, 'en_US', " + TimeUtil.now() + ")");

            UserStats stats = sqlUtil.getUserData(p.getUniqueId());
            WarriorUser user = new WarriorUser(p, stats);
            userCache.add(user);
        }

        if(debugger != null) {
            debugger.end();
        }

    }

    private final String[] warnMessage = {
        "===================== [ §3WARRIOR§r ] =====================",
        "|    WARNING: YOUR SERVER DOES NOT RUN A SUPPORTED    |",
        "|            JAVA AND/OR MINECRAFT VERSION            |",
        "=======================================================", " ",
        "\033[0;1mJava 11\u001b[0m and \033[0;1mPaperMC 1.13-1.16.3\u001b[0m is recommended.",
        "Bug reports for earlier/newer versions of Minecraft or",
        "Java will be ignored.", " ",
    };

    private void sendWarnMessage() {
        for(String s : warnMessage) {
            apiProvider.getLogger().warn(s);
        }

        String minecraftDetected = String.format("\033[0;1mYour Minecraft Version:\u001b[0m %s (%s)",
                apiProvider.getServerVersion().toString(), apiProvider.getPlatform().toString());

        apiProvider.getLogger().warn("\033[0;1mYour Java Version:      \u001b[0m" + apiProvider.getJavaVersion());
        apiProvider.getLogger().warn(minecraftDetected);
        apiProvider.getLogger().warn("===================== [ §3WARRIOR§r ] =====================");
    }

    private boolean checkSupported() {
        if(apiProvider.getServerVersion().MINOR < 13) return false;
        return apiProvider.getJavaVersion().startsWith("11");
    }

    @Override
    public void onDisable() {
        userCache.clear();
        sqlUtil.close();
    }

    protected void getCommandMap() {
        Field field;
        try {
            field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            cMap = (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException err) {
            pluginLogger.error("Could not register commands!");
        }
    }

    public static Translator getTranslator() {
        return translator;
    }

    public Object getUtil(String s) {
        if(s.contains("sql")) return sqlUtil;
        return null;
    }

    public Object getManager(String s) {
        if(s.contains("lobby")) return lobbyManager;
        if(s.contains("motd")) return motdManager;
        return null;
    }

    public static Plugin getPlugin() {
        return getPlugin(Warrior.class);
    }

    public boolean usePAPI() {
        return usePlaceholderAPI;
    }

    public static HibiscusAPI getAPI() {
        return apiProvider;
    }
}