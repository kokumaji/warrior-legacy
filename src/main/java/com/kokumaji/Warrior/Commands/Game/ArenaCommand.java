package com.kokumaji.Warrior.Commands.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kokumaji.Warrior.Game.Objects.WarriorUser;
import com.kokumaji.Warrior.Warrior;
import com.kokumaji.Warrior.Game.Managers.ArenaManager;
import com.kokumaji.Warrior.Game.Managers.UserManager;
import com.kokumaji.Warrior.Game.Objects.Arena;
import me.kokumaji.HibiscusAPI.api.command.AsyncCommand;
import com.kokumaji.Warrior.Game.Objects.GUIs.ArenaGUI;
import com.kokumaji.Warrior.Game.Objects.GUIs.GUIHandler;
import com.kokumaji.Warrior.Utils.ConfigUtil;
import com.kokumaji.Warrior.Utils.MessageUtil;

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
        WarriorUser u = UserManager.GetPlayer(((Player) sender).getUniqueId());
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
                }
            });

            u.sendMessage(translator.Translate(u.bukkit(), "arena-messages.arena-teleport", true, new HashMap<>(){
                private static final long serialVersionUID = 1L;

                {
                    put("Arena", args[1]);
                }
            }));
            
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

            Arena aNew = new Arena(args[1], 0, u.bukkit().getLocation(), 16);
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
