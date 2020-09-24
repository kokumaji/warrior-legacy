package com.kokumaji.StickyPvP.Commands.Game;

import java.util.List;

import com.kokumaji.StickyPvP.StickyPvP;
import com.kokumaji.StickyPvP.Game.Managers.KitManager;
import com.kokumaji.StickyPvP.Game.Managers.UserManager;
import com.kokumaji.StickyPvP.Game.Objects.Kit;
import com.kokumaji.StickyPvP.Game.Objects.User;
import me.kokumaji.HibiscusAPI.api.command.AsyncCommand;
import com.kokumaji.StickyPvP.Utils.MessageUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class KitCommand extends AsyncCommand implements TabCompleter {

    StickyPvP self;

    public KitCommand(String commmandName, Plugin plugin) {
        super(commmandName, plugin);
        this.self = (StickyPvP) plugin;
        this.setTabCompleter(this);
    }

    @Override
    public void onSyntaxError(CommandSender sender, String label, String[] args, int exitCode) {
        User u = UserManager.GetPlayer(((Player) sender).getUniqueId());
        switch (exitCode) {
            case 1:
                MessageUtil.UnknownSubCommand(u);
                break;
            case 2:
                MessageUtil.UnknownArgument(u, args[args.length - 1]);
                break;
            case 3:
                MessageUtil.MissingArguments(u);
        }
    }

    @Override
    public int Execute(CommandSender sender, String commandLabel, String[] args) {
        User u = UserManager.GetPlayer(((Player) sender).getUniqueId());

        if(u.IsInLobby()) return 0;

        if(args.length == 0) {
            // kit gui stuff
        } else if(args[0].equalsIgnoreCase("select")) {
            Kit k = KitManager.GetKit(args[1]);
            
            if(k == null) 
                return 2;

            KitManager.SetKit(u, k);
        }
        return 0;
    }

    @Override
    public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        return null;
    }

    @Override
    public Plugin getPlugin() {
        return self;
    }


    
}
