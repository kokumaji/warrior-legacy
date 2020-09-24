package com.kokumaji.StickyPvP.Game.Objects;

import com.kokumaji.StickyPvP.Utils.LanguageCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class UserStats {
    public final UUID PLAYER_UUID;
    public final int KILLS;
    public final int DEATHS;
    public final int COINS;
    public final LanguageCode LANG;

    public UserStats(UUID uuid, int pKills, int pDeaths, int pCoins, LanguageCode languageCode) {
        this.PLAYER_UUID = uuid;
        this.KILLS = pKills;
        this.DEATHS = pDeaths;
        this.COINS = pCoins;
        this.LANG = languageCode;

    }

    public double getKDR() {
        if(DEATHS==0) return KILLS;

        BigDecimal bd = BigDecimal.valueOf((double)KILLS / DEATHS);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
