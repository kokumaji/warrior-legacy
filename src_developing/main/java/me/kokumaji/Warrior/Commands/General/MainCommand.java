package me.kokumaji.Warrior.Commands.General;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.kokumaji.HibiscusAPI.api.objects.GenericItem;
import me.kokumaji.HibiscusAPI.api.particle.Orientation;
import me.kokumaji.HibiscusAPI.api.particle.ParticleSystem;
import me.kokumaji.HibiscusAPI.api.particle.shapes.Circle;
import me.kokumaji.HibiscusAPI.api.util.MathUtil;
import me.kokumaji.HibiscusAPI.api.util.MojangUtil;
import me.kokumaji.Warrior.Game.Objects.GUIs.ClassGUI;
import me.kokumaji.Warrior.Game.Objects.GUIs.GUIHandler;
import me.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.Warrior.Utils.MessageUtil;
import me.kokumaji.Warrior.Utils.ProgressBar;
import me.kokumaji.Warrior.Warrior;
import me.kokumaji.Warrior.Game.Managers.MOTDManager;

import me.kokumaji.HibiscusAPI.api.translation.ChatMessage;
import me.kokumaji.HibiscusAPI.api.translation.Translator;

import me.kokumaji.Warrior.Utils.ConfigUtil;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import me.kokumaji.HibiscusAPI.api.command.AsyncCommand;
import org.bukkit.util.Vector;

public class MainCommand extends AsyncCommand implements TabCompleter {

    private Warrior self;

    private Translator translator;

    private final String[] baseArguments = {"commands", "class", "motd", "placeholder", "reload"};
    private final String[] reloadArguments = {"config", "motd"};

    public MainCommand(String commandName, Plugin plugin) {
        super(commandName, plugin);

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
        WarriorUser user = Warrior.getUserCache().get(((Player) sender).getUniqueId());
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
                MOTDManager mm = (MOTDManager) self.getManager("motd");
                mm.ReloadMOTD();

                user.sendMessage(msg);
            }

        } else if(args[0].equalsIgnoreCase("commands")) {
            user.sendMessage("send commands gui");
        } else if(args[0].equalsIgnoreCase("motd")) {
            MOTDManager mm = (MOTDManager) self.getManager("motd");
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
            ParticleSystem ps = new ParticleSystem(self, user.bukkit().getLocation());
            ps.shape(Particle.BLOCK_CRACK, new Circle(0, 0, 0, 1, Orientation.XZ), Material.REDSTONE_BLOCK.createBlockData());

            ItemStack itemStack = new GenericItem(Material.PLAYER_HEAD, 1).build();
            MojangUtil.applyURL("kokumaji", itemStack);

            user.bukkit().getInventory().addItem(itemStack);

            user.sendActionBar("Test Message");

            /*ChatMessage msg = new ChatMessage("&3&lClick me for a suprise!")
                    .setClickAction(self, user.bukkit(), 20, player1 -> {
                        Location loc = user.bukkit().getLocation();
                        ArrayList<Entity> removeLater = new ArrayList<>();
                        Material[] itemSet = {Material.CORNFLOWER, Material.SUNFLOWER, Material.ORANGE_TULIP, Material.ROSE_BUSH, Material.PINK_TULIP, Material.PEONY, Material.POPPY, Material.OXEYE_DAISY};

                        user.playSound(Sound.ENTITY_GENERIC_EXPLODE);

                        Bukkit.getScheduler().runTask(self, () -> {
                            ParticleSystem ps = new ParticleSystem(self, loc);

                            for(int i = 0; i < 8; i++) {
                                ItemStack item = new GenericItem(itemSet[MathUtil.rInt(itemSet.length)], 1).build();
                                ItemMeta itemMeta = item.getItemMeta();

                                double randomX = MathUtil.rDouble(-0.10, 0.10);
                                double randomY = MathUtil.rDouble(0.30, 0.50);
                                double randomZ = MathUtil.rDouble(-0.10, 0.10);

                                itemMeta.setDisplayName(ConfigUtil.GenerateID(8));
                                item.setItemMeta(itemMeta);

                                Item ent = user.bukkit().getWorld().dropItem(loc, item);
                                ent.setVelocity(new Vector(randomX, randomY, randomZ));
                                ent.setPickupDelay(Integer.MAX_VALUE);

                                removeLater.add(ent);

                                ps.spawn(Particle.VILLAGER_HAPPY, randomX * 10, randomY * 4, randomZ * 10, 1);
                            }
                        });

                        Bukkit.getScheduler().runTaskLater(self, () -> {
                            for(Entity e : removeLater) {
                                e.remove();
                            }
                        }, 100L);
                    });

            user.sendMessage(msg);*/

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