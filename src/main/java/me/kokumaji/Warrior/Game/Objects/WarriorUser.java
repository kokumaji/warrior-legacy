package me.kokumaji.Warrior.Game.Objects;

import lombok.Getter;
import lombok.Setter;
import me.kokumaji.HibiscusAPI.api.storage.cache.Cacheable;
import me.kokumaji.Warrior.Warrior;
import me.kokumaji.Warrior.Utils.ConfigUtil;
import me.kokumaji.Warrior.Utils.DatabaseUtil;
import me.kokumaji.Warrior.Utils.LanguageCode;

import me.kokumaji.HibiscusAPI.api.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class WarriorUser extends User implements Cacheable {

    private LanguageCode lang;
    private Arena arena;
    private boolean lobby;

    @Getter
    double coinMultiplier = 1.0;

    private int killstreak = 0;
    @Getter int kills;
    @Getter private int deaths;
    @Getter private int coins;
    @Getter @Setter
    boolean spectating;

    public WarriorUser(Player player, UserStats pStats) {
        super(player);
        lang = ConfigUtil.GetDefaultLang();

        //FIXME: ADD DATABASE IMPORT
        kills = pStats.KILLS;
        deaths = pStats.DEATHS;
        coins = pStats.COINS;
    }

    public void resetStreak() {
        killstreak = 0;
    }

    public void addDeath() {
        deaths++;
    }

    public void addKillStreak() {
        killstreak++;
    }

    public void addKill() {
        kills++;
    }

    public void setCoins(int amount) {
        this.coins = amount;
    }

    public void addCoins(int amount, boolean useMultiplier) {
        this.coins = useMultiplier ? coins + (int)(amount * coinMultiplier) : coins + amount;
    }

    public void takeCoins(int amount) {
        int result = coins - amount;
        if(coins == 0) return;

        if(result < 0) {
            coins = 0;
            return;
        }

        coins = result;
    }

    public boolean canPay(int amount) {
        return coins - amount > 0;
    }

    public LanguageCode getLang() {
        return this.lang;
    }

    public void setArena(Arena pArena) {
        this.arena = pArena;
    }

    public Arena getArena() {
        return this.arena;
    }

    public int getStreak() {
        return killstreak;
    }

    public double getKDR() {
        if(deaths == 0) return kills;

        BigDecimal bd = BigDecimal.valueOf((double)kills / deaths);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void saveData(Warrior pl) {
        DatabaseUtil dbUtil = (DatabaseUtil)pl.getUtil("sql");
        dbUtil.executeUpdate("UPDATE $table_player_stats SET kills = " + kills +
                                ", deaths = " + deaths +
                                ", coins = " + coins +
                                ", lang = '" + lang +
                                "' WHERE uuid = '" + getUUID() + "'");
    }

    public boolean isInLobby() {
        return lobby;
    }

    public void inLobby(boolean b) {
        this.lobby = b;
    }

    public UserStats getStats() {
        return new UserStats(getUUID(), kills, deaths, coins, lang, getLastLogin());
    }

    @Override
    public UUID getKey() {
        return getUUID();
    }

    public void sendCountdown(int seconds) {
        new BukkitRunnable() {
            int i = seconds;
            @Override
            public void run() {
                if(i > 0) {


                    bukkit().sendTitle(String.valueOf(i), "", 2, 20, 2);
                    i--;
                } else cancel();

            }
        }.runTaskTimer(Warrior.getPlugin(), 0L, 20L);
    }
}
