package me.kokumaji.Warrior.Commands.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.kokumaji.Warrior.Game.Managers.LobbyManager;
import me.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.Warrior.Warrior;
import me.kokumaji.Warrior.Game.Managers.ArenaManager;
import me.kokumaji.Warrior.Game.Objects.Arena;
import me.kokumaji.HibiscusAPI.api.command.AsyncCommand;
import me.kokumaji.Warrior.Game.Objects.GUIs.ArenaGUI;
import me.kokumaji.Warrior.Game.Objects.GUIs.GUIHandler;
import me.kokumaji.Warrior.Utils.ConfigUtil;

import me.kokumaji.HibiscusAPI.api.translation.Translator;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ArenaCommand extends AsyncCommand implements TabCompleter {

    Warrior self;

    private Translator translator;

    public ArenaCommand(String commmandName, Plugin plugin) {
        super(commmandName, plugin);
        this.self = (Warrior) plugin;

        this.translator = Warrior.getTranslator();
        this.setTabCompleter(this);
    }

    @Override
    public void onSyntaxError(CommandSender sender, String label, String[] args) {

    }

    @Override
    public boolean Execute(CommandSender sender, String commandLabel, String[] args) {
        WarriorUser u = Warrior.getUserCache().get(((Player) sender).getUniqueId());
        FileConfiguration c = ConfigUtil.GetConfig(ConfigUtil.ConfigType.SETTINGS);

        if (args.length == 0) {
            ArenaGUI aGUI = (ArenaGUI) GUIHandler.GetGUI("arena");

            Bukkit.getScheduler().runTask(self, new Runnable() {
                @Override
                public void run() {
                    aGUI.BuildGUI(u.bukkit());
                }
            });
        } else if (args[0].equalsIgnoreCase("join")) {
            if (args.length < 2)
                return true;

            if(u.getArena() != null) {
                u.sendMessage(translator.Translate("arena-messages.already-joined", true));
                return true;
            }

            Arena a = ArenaManager.GetArena(args[1]);

            if (a == null)
                return true;

            Bukkit.getScheduler().runTask(self, new Runnable() {
                @Override
                public void run() {
                   a.Teleport(u);

                   Sound s = null;

                   if(!c.getString("arena-settings.play-teleport-sound").toLowerCase().equals("none"))
                       s = Sound.valueOf(c.getString("arena-settings.play-teleport-sound").toUpperCase());
                   
                   u.playSound(s);
                   u.setArena(a);
                }
            });

            u.sendMessage(translator.Translate(u.bukkit(), "arena-messages.arena-teleport", true, new HashMap<>(){
                private static final long serialVersionUID = 1L;

                {
                    put("Arena", args[1]);
                }
            }));
            
        } else if(args[0].equalsIgnoreCase("leave")) {
            LobbyManager lm = (LobbyManager) self.getManager("lobby");

            if(u.getArena() == null) {
                u.sendMessage(translator.Translate("lobby-messages.already-in-lobby", true));
                return true;
            }

            Bukkit.getScheduler().runTask(self, new Runnable() {
                @Override
                public void run() {

                    Sound s = null;

                    if(!c.getString("arena-settings.play-teleport-sound").toLowerCase().equals("none"))
                        s = Sound.valueOf(c.getString("arena-settings.play-teleport-sound").toUpperCase());

                    lm.TeleportPlayer(u);
                    u.playSound(s);
                }
            });

            u.sendMessage(translator.Translate(u.bukkit(), "arena-messages.arena-leave", true, new HashMap<>(){
                private static final long serialVersionUID = 1L;

                {
                    put("Arena", u.getArena().GetName());
                }
            }));

            u.setArena(null);

        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) 
                return true;

            Arena a = ArenaManager.GetArena(args[1]);
            
            if(a != null) {
                u.sendMessage(translator.Translate(u.bukkit(), "arena-messages.arena-already-exists", true, new HashMap<>(){
                    private static final long serialVersionUID = 1L;
    
                    {
                        put("Arena", args[1]);
                    }
                }));
                return true;
            }

            Arena aNew = new Arena(args[1], ArenaManager.GetArenas().size(), u.bukkit().getLocation(), 16);
            ArenaManager.RegisterArena(aNew);
            aNew.Save();

            u.sendMessage(translator.Translate(u.bukkit(), "arena-messages.arena-creation", true, new HashMap<>(){
                private static final long serialVersionUID = 1L;

                {
                    put("Arena", args[1]);
                }
            }));

            return true;
        } else {
            return true;
        }

        return true;
    }

    @Override
    public Plugin getPlugin() {
        return self;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> options = new ArrayList<String>();
        if(cmd.equals(this)) {
            if(args.length == 1) {
                options.add("join");
                options.add("create");
            }
        }
        return options;
    }

    
}
