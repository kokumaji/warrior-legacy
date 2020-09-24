package com.kokumaji.StickyPvP.Commands.General;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kokumaji.StickyPvP.StickyPvP;
import com.kokumaji.StickyPvP.Game.Managers.MOTDManager;
import com.kokumaji.StickyPvP.Game.Managers.UserManager;
import com.kokumaji.StickyPvP.Game.Objects.User;
import com.kokumaji.StickyPvP.Utils.ConfigUtil;
import com.kokumaji.StickyPvP.Utils.MessageUtil;
import com.kokumaji.StickyPvP.Utils.TranslationsUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.kokumaji.HibiscusAPI.api.command.AsyncCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class MainCommand extends AsyncCommand implements TabCompleter {

    private StickyPvP self;

    private String[] baseArguments = {"commands", "motd", "reload"};
    private String[] reloadArguments = {"config", "motd"};

    public MainCommand(String commmandName, Plugin plugin) {
        super(commmandName, plugin);

        setDescription("Default KitPvP Command");
        this.self = (StickyPvP) plugin;

        this.setTabCompleter(this);
    }

    @Override
    public void onSyntaxError(CommandSender sender, String label, String[] args, int exitCode) {
        User u = UserManager.GetPlayer(((Player) sender).getUniqueId());
        MessageUtil.UnknownSubCommand(u);
    }

    @Override
    public int Execute(CommandSender sender, String commandLabel, String[] args) {
        User u = UserManager.GetPlayer(((Player) sender).getUniqueId());

        if(args.length == 0) {
            TextComponent github = new TextComponent("§7Developed by §bKokumaji");
            github.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/kokumaji"));
            github.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§bVisit GitHub Page")));
            MessageUtil.CenterMessage(u.Bukkit(), " ", TranslationsUtil.HL, " ", "&3&lStickyPvP &8v1.0", github, "&7Type &b/stickypvp help &7for commands.", " ", TranslationsUtil.HL, " ");

        } else if(args[0].equalsIgnoreCase("reload")) {
            if(args.length < 2 || args[1].equalsIgnoreCase("config")) {
                String msg = TranslationsUtil.Translate("commands.reload-message", true);
                ConfigUtil.ReloadConfig(ConfigUtil.ConfigType.SETTINGS);
                u.SendMessage(msg);
            } else if(args[1].equalsIgnoreCase("motd")) {
                String msg = TranslationsUtil.Translate("commands.motd-reload-message", true);
                MOTDManager mm = (MOTDManager) self.GetManager("motd");
                mm.ReloadMOTD();

                u.SendMessage(msg);
            }

        } else if(args[0].equalsIgnoreCase("commands")) {
            u.SendMessage("send commands gui");
        } else if(args[0].equalsIgnoreCase("motd")) {
            MOTDManager mm = (MOTDManager) self.GetManager("motd");
            mm.SendMOTD(u);
        } else {
            return 1;
        } 

        return 0;
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
            }
        }
        return options;
    }
    
}