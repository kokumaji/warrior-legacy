package me.kokumaji.Warrior.Commands.Game;

import java.util.List;

import me.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.Warrior.Warrior;
import me.kokumaji.Warrior.Game.Managers.KitManager;
import me.kokumaji.Warrior.Game.Objects.Kit;
import me.kokumaji.HibiscusAPI.api.command.AsyncCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class KitCommand extends AsyncCommand implements TabCompleter {

    Warrior self;

    public KitCommand(String commmandName, Plugin plugin) {
        super(commmandName, plugin);
        this.self = (Warrior) plugin;
        this.setTabCompleter(this);
    }

    @Override
    public void onSyntaxError(CommandSender sender, String label, String[] args) {

    }

    @Override
    public boolean Execute(CommandSender sender, String commandLabel, String[] args) {
        WarriorUser u = Warrior.getUserCache().get(((Player) sender).getUniqueId());

        if(u.isInLobby()) return true;

        if(args.length == 0) {
            // kit gui stuff
        } else if(args[0].equalsIgnoreCase("select")) {
            Kit k = KitManager.GetKit(args[1]);
            
            if(k == null) 
                return true;

            KitManager.SetKit(u, k);
        }
        return true;
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
