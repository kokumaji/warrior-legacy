package me.kokumaji.Warrior.Game.Managers;

import me.kokumaji.HibiscusAPI.api.storage.cache.Cache;
import me.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.Warrior.Warrior;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SpectateManager {

    private static final Cache<WarriorUser> cached = new Cache<>();

    private static int respawnDelay = 3;

    private static Team ghostTeam = null;

    private static final String SPECTATOR_TEAM = "warriorSpectator";

    public SpectateManager(FileConfiguration c) {
        respawnDelay = c.getInt("arena-settings.respawn-delay");
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        if(sb.getTeam(SPECTATOR_TEAM) == null) {
            ghostTeam = sb.registerNewTeam(SPECTATOR_TEAM);
            ghostTeam.setAllowFriendlyFire(false);
            ghostTeam.setCanSeeFriendlyInvisibles(true);
        } else {
            ghostTeam = sb.getTeam(SPECTATOR_TEAM);
        }
    }

    public static boolean addPlayer(WarriorUser user, SpectateReason reason) {
        Player p = user.bukkit();
        cached.add(user);
        switch(reason) {
            case DEATH:
                p.setHealth(20);
                p.setFireTicks(0);
                p.setFoodLevel(20);

                ghostTeam.addEntry(p.getName());

                p.setGameMode(GameMode.SURVIVAL);
                p.setAllowFlight(true);
                p.setFlying(true);
                for(PotionEffect potion : p.getActivePotionEffects()) {
                    p.removePotionEffect(potion.getType());
                }

                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2));

                new BukkitRunnable() {

                    int i = 3;

                    @Override
                    public void run() {
                        if(i != 0) {
                            p.sendTitle("§4§lYOU DIED!", "§7You will respawn in " + i + " seconds.");
                            user.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
                            i--;
                        } else {
                            p.setGameMode(GameMode.SURVIVAL);
                            p.teleport(p.getWorld().getSpawnLocation());
                            user.playSound(Sound.BLOCK_ENCHANTMENT_TABLE_USE);
                            p.sendTitle(" ", " ");
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            p.removePotionEffect(PotionEffectType.INVISIBILITY);

                            ghostTeam.removeEntry(p.getName());

                            removePlayer(user);
                            cancel();
                        }
                    }
                }.runTaskTimer(Warrior.getPlugin(), 0L, 20L);
                return true;
            case COMMAND:
                return true;
        }

        return false;
    }

    public static void removePlayer(WarriorUser user) {
        if(cached.contains(user.getKey())) cached.remove(user);
    }

}
