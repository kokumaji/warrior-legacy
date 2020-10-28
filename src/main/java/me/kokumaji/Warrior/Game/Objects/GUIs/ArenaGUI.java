package me.kokumaji.Warrior.Game.Objects.GUIs;

import me.kokumaji.Warrior.Game.Managers.ArenaManager;
import me.kokumaji.Warrior.Game.Objects.Arena;
import me.kokumaji.Warrior.Game.Objects.GUI;
import me.kokumaji.Warrior.Game.Objects.WarriorUser;

import me.kokumaji.Warrior.Warrior;
import me.kokumaji.HibiscusAPI.api.objects.CustomItem;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class ArenaGUI extends GUI {

    public ArenaGUI(int pSize, Plugin pPlugin) {
        super(pSize, pPlugin);
        Bukkit.getServer().getPluginManager().registerEvents(this, pPlugin);
    }

    @Override
    public void BuildGUI(Player player) {
        this.MakeInventory("Arenas");
        ItemStack placeholder = new CustomItem(Material.BLACK_STAINED_GLASS_PANE, 1, " ").build();
        for(int i = 0; i < 9; i++) {
            RegisterSlot(placeholder, i);
            RegisterSlot(placeholder, (getInventory().getSize() - 9) +  i);
        }

        int j = 9;
        for(Arena a : ArenaManager.GetArenas().values()) {
            if(j > 36) break;
            ItemStack arenaItem = new CustomItem(Material.FILLED_MAP, 1, "§3§l§o" + a.GetName())
                    .setLore("§7Teleport to Arena §b" + a.GetName()).build();

            RegisterSlot(arenaItem, j);
            j++;
        }

        player.openInventory(getInventory());

    }

    @Override
    @EventHandler
    protected void onSlotClick(InventoryClickEvent e) {
        int slot = e.getSlot();

        if(slot < 0) return;

        if (e.getInventory() != getInventory())
            return;

        if(IsValidSlot(slot)) {
            e.setCancelled(true);
            if(slot > 8 && slot < 37) {
                ItemStack is = e.getCurrentItem();
                WarriorUser u = Warrior.getUserCache().get(e.getWhoClicked().getUniqueId());
                Arena a = ArenaManager.GetArena(is.getItemMeta().getDisplayName().substring(6));

                Bukkit.getScheduler().runTask(Warrior.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        a.Teleport(u);

                        Sound s = null;

                        FileConfiguration c = Warrior.getPlugin().getConfig();

                        if(!c.getString("arena-settings.play-teleport-sound").toLowerCase().equals("none"))
                            s = Sound.valueOf(c.getString("arena-settings.play-teleport-sound").toUpperCase());

                        u.playSound(s);
                        u.setArena(a);
                    }
                });

                Translator t = Warrior.getTranslator();

                u.sendMessage(t.Translate(u.bukkit(), "arena-messages.arena-teleport", true, new HashMap<>(){
                    private static final long serialVersionUID = 1L;

                    {
                        put("Arena", a.GetName());
                    }
                }));
            }
        }
    }
    
}
