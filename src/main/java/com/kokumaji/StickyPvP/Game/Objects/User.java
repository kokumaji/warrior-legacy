package com.kokumaji.StickyPvP.Game.Objects;

import com.kokumaji.StickyPvP.StickyPvP;
import com.kokumaji.StickyPvP.Utils.ConfigUtil;
import com.kokumaji.StickyPvP.Utils.DatabaseUtil;
import com.kokumaji.StickyPvP.Utils.InternalMessages;
import com.kokumaji.StickyPvP.Utils.LanguageCode;
import com.kokumaji.StickyPvP.Utils.ConfigUtil.ConfigType;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class User {

    private UUID uuid;
    private String username;
    private Player player;
    private LanguageCode lang;
    private Arena arena;
    private boolean lobby;

    private int killstreak = 0;
    private int kills;
    private int deaths;
    private int coins;

    public User(Player pPlayer, UserStats pStats) {
        this.uuid = pPlayer.getUniqueId();
        this.username = pPlayer.getName();
        this.player = pPlayer;

        lang = ConfigUtil.GetDefaultLang();

        //FIXME: ADD DATABASE IMPORT
        kills = pStats.KILLS;
        deaths = pStats.DEATHS;
        coins = pStats.COINS;
    }

    public Player Bukkit() {
        return player;
    }

    public void SendMessage(String pMessage) {
        player.sendMessage(pMessage);
    }

    public void ResetStreak() {
        killstreak = 0;
    }

    public void AddDeath() {
        deaths++;
    }

    public void AddKillStreak() {
        killstreak++;
    }

    public void AddKill() {
        kills++;
    }

    public void SetCoins(int amount) {
        this.coins = amount;
    }

    public void AddCoins(int amount) {
        this.coins = coins + amount;
    }

    public void TakeCoins(int amount) {
        int result = coins - amount;
        if(coins == 0) return;

        if(result < 0) {
            coins = 0;
            return;
        }

        coins = result;
    }

    public boolean CanPay(int amount) {
        return coins - amount > 0;
    }

    public String GetUsername() {
        return this.username;
    }

    public UUID GetUUID() {
        return this.uuid;
    }

    public LanguageCode GetLang() {
        return this.lang;
    }

    public void SetArena(Arena pArena) {
        this.arena = pArena;
    }

    public Arena GetArena() {
        return this.arena;
    }

    public int GetStreak() {
        return killstreak;
    }

    public int GetKills() {
        return this.kills;
    }

    public int GetDeaths() {
        return this.deaths;
    }

    public int GetCoins() {
        return coins;
    }

    public double GetKDR() {
        if(deaths == 0) return kills;

        BigDecimal bd = BigDecimal.valueOf((double)kills / deaths);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void SaveData(StickyPvP pl) {
        DatabaseUtil dbUtil = (DatabaseUtil)pl.GetUtil("sql");
        dbUtil.ExecuteUpdate("UPDATE $table_player_stats SET kills = " + kills +
                                ", deaths = " + deaths +
                                ", coins = " + coins +
                                ", lang = '" + lang +
                                "' WHERE uuid = '" + uuid + "'");
    }

    public void Teleport(Location loc) {
        if(loc == null) {
            FileConfiguration cnf = ConfigUtil.GetConfig(ConfigType.SETTINGS);
            if(cnf.getBoolean("general-settings.debug"))
                InternalMessages.TELEPORT_LOCATION_NULL.Log(this.username);
            return;
        }
        player.teleport(loc);
    }

    public void PlaySound(Sound sound) {
        if(sound == null) return;
        player.playSound(player.getLocation(), sound, 1f, 1f);
    }

    public boolean IsInLobby() {
        return lobby;
    }

    public void InLobby(boolean b) {
        this.lobby = b;
    }

    public UserStats GetStats() {
        return new UserStats(uuid, kills, deaths, coins, lang);
    }
}
