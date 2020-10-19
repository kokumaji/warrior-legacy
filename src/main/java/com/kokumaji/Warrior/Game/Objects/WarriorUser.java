package com.kokumaji.Warrior.Game.Objects;

import com.kokumaji.Warrior.Warrior;
import com.kokumaji.Warrior.Utils.ConfigUtil;
import com.kokumaji.Warrior.Utils.DatabaseUtil;
import com.kokumaji.Warrior.Utils.LanguageCode;

import me.kokumaji.HibiscusAPI.api.objects.User;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WarriorUser extends User {

    private LanguageCode lang;
    private Arena arena;
    private boolean lobby;

    private int killstreak = 0;
    private int kills;
    private int deaths;
    private int coins;

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

    public void addCoins(int amount) {
        this.coins = coins + amount;
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

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getCoins() {
        return coins;
    }

    public double getKDR() {
        if(deaths == 0) return kills;

        BigDecimal bd = BigDecimal.valueOf((double)kills / deaths);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void saveData(Warrior pl) {
        DatabaseUtil dbUtil = (DatabaseUtil)pl.GetUtil("sql");
        dbUtil.ExecuteUpdate("UPDATE $table_player_stats SET kills = " + kills +
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
        return new UserStats(getUUID(), kills, deaths, coins, lang);
    }

}
