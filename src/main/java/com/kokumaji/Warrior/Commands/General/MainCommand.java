package com.kokumaji.Warrior.Commands.General;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.kokumaji.Warrior.Game.Objects.GUIs.ClassGUI;
import com.kokumaji.Warrior.Game.Objects.GUIs.GUIHandler;
import com.kokumaji.Warrior.Game.Objects.WarriorUser;
import com.kokumaji.Warrior.Utils.Hologram;
import com.kokumaji.Warrior.Warrior;
import com.kokumaji.Warrior.Game.Managers.MOTDManager;
import com.kokumaji.Warrior.Game.Managers.UserManager;
import com.kokumaji.Warrior.Utils.ConfigUtil;
import com.kokumaji.Warrior.Utils.MessageUtil;

import me.kokumaji.HibiscusAPI.api.particle.ParticleSystem;
import me.kokumaji.HibiscusAPI.api.particle.shape.Circle;
import me.kokumaji.HibiscusAPI.api.translation.ChatMessage;
import me.kokumaji.HibiscusAPI.api.translation.Translator;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.kokumaji.HibiscusAPI.api.command.AsyncCommand;


public class MainCommand extends AsyncCommand implements TabCompleter {

    private Warrior self;

    private Translator translator;

    private String[] baseArguments = {"commands", "motd", "placeholder", "reload"};
    private String[] reloadArguments = {"config", "motd"};

    public MainCommand(String commmandName, Plugin plugin) {
        super(commmandName, plugin);

        setDescription("Default KitPvP Command");
        this.self = (Warrior) plugin;
        this.translator = Warrior.getTranslator();
        this.setTabCompleter(this);
    }

    @Override
    public void onSyntaxError(CommandSender sender, String label, String[] args) {

    }

    @Override
    public boolean Execute(CommandSender sender, String commandLabel, String[] args) {
        WarriorUser user = UserManager.GetPlayer(((Player) sender).getUniqueId());
        OfflinePlayer player = user.bukkit();

        if(args.length == 0) {
            ChatMessage github = new ChatMessage("§7Developed by §bKokumaji")
                    .setLink("https://github.com/kokumaji")
                    .setTooltip(translator.parsePlaceholder(player, Translator.applyColor("&b&l{char:arrow_up} &7Visit GitHub Page")));

            MessageUtil.CenterMessage(user.bukkit(), " ", MessageUtil.HL, " ", "&3&lWarrior &8v1.0", github.getComponent(), "&7Type &b/warrior commands &7for commands.", " ", MessageUtil.HL, " ");

        } else if(args[0].equalsIgnoreCase("reload")) {
            if(args.length < 2 || args[1].equalsIgnoreCase("config")) {
                String msg = translator.Translate(player,"command.reload-message", true);
                ConfigUtil.ReloadConfig(ConfigUtil.ConfigType.SETTINGS);
                user.sendMessage(msg);
            } else if(args[1].equalsIgnoreCase("motd")) {
                String msg = translator.Translate(player, "command.motd-reload-message", true);
                MOTDManager mm = (MOTDManager) self.GetManager("motd");
                mm.ReloadMOTD();

                user.sendMessage(msg);
            }

        } else if(args[0].equalsIgnoreCase("commands")) {
            user.sendMessage("send commands gui");
        } else if(args[0].equalsIgnoreCase("motd")) {
            MOTDManager mm = (MOTDManager) self.GetManager("motd");
            mm.SendMOTD(user);
        } else if(args[0].equalsIgnoreCase("placeholder")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            String placeholder = args[2];

            String parsed = translator.Translate(user.bukkit(), "command.parse-placeholder", true) + translator.parsePlaceholder(target, placeholder);
            user.sendMessage(parsed);
        } else if(args[0].equalsIgnoreCase("class")) {
            ClassGUI aGUI = (ClassGUI) GUIHandler.GetGUI("class");

            Bukkit.getScheduler().runTask(self, new Runnable() {
                @Override
                public void run() {
                    aGUI.BuildGUI(user.bukkit());
                }
            });
        } else if(args[0].equalsIgnoreCase("debug")) {
            Hologram hg = new Hologram(user.bukkit().getLocation(), "Test Hologram");

            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            user.sendActionBar(translator.parsePlaceholder(user.bukkit(), message));

        } else if(args[0].equalsIgnoreCase("effect")) {
            ParticleSystem ps = new ParticleSystem(self, user.bukkit().getLocation());
            ps.setParticle(Particle.LAVA);
            ps.shape(new Circle(0, 0, 0, 3), ps.getParticle());

                        /*ps.parametric2(Particle.valueOf(args[1]), 1, new ParticleSystem.Parametric2() {

                        @Override
                        public double x(double v, double v1) {
                            return Math.cos(v) * Math.sin(v1);
                        }

                        @Override
                        public double y(double v, double v1) {
                            return Math.sin(v) * Math.cos(v1);
                        }

                        @Override
                        public double z(double v, double v1) {
                            return Math.cos(v);
                        }
                    },
                    0,
                    Math.PI * 2,
                    0, Math.PI * 2, Math.PI/32 * 2, Math.PI/32 * 2
            );*/

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
                if(!args[0].equals("")) {
                    for(String s : baseArguments) {
                        if(s.toLowerCase().startsWith(args[0].toLowerCase())) {
                            options.add(s);
                        }
                    }
                } else {
                    return Arrays.asList(baseArguments);
                }
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(!args[1].equals("")) {
                    for(String s : reloadArguments) {
                        if(s.toLowerCase().startsWith(args[1].toLowerCase())) {
                            options.add(s);
                        }
                    }
                } else {
                    return Arrays.asList(reloadArguments);
                }
            } else if(args[0].equalsIgnoreCase("placeholder")) {
                if(args.length > 2) return null;
                if(!args[1].equals("")) {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if(p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            options.add(p.getName());
                        }
                    }
                } else {
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                }
            }
        }
        return options;
    }
    
}