package me.kokumaji.Warrior.Game.Objects;

import lombok.Getter;
import me.kokumaji.Warrior.Utils.LanguageCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class UserStats {

    public final UUID PLAYER_UUID;
    public final int KILLS;
    public final int DEATHS;
    public final int COINS;
    public final LanguageCode LANG;
    public final long LAST_LOGIN;

    public UserStats(UUID uuid, int pKills, int pDeaths, int pCoins, LanguageCode languageCode, long pLogin) {
        this.PLAYER_UUID = uuid;
        this.KILLS = pKills;
        this.DEATHS = pDeaths;
        this.COINS = pCoins;
        this.LANG = languageCode;

        this.LAST_LOGIN = pLogin;

    }

    public double getKDR() {
        if(DEATHS==0) return KILLS;

        BigDecimal bd = BigDecimal.valueOf((double)KILLS / DEATHS);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
