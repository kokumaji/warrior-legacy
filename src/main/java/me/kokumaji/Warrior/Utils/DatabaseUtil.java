package me.kokumaji.Warrior.Utils;

import me.kokumaji.Warrior.Game.Objects.UserStats;
import me.kokumaji.HibiscusAPI.api.storage.DatabaseConnection;
import me.kokumaji.Warrior.Warrior;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseUtil extends DatabaseConnection {

    public DatabaseUtil(FileConfiguration fc, ConnectionDetails details) {
        super(fc, details);
    }

    @Override
    public void makeTables() {
        if(isOpen()) {
            String stmt = "CREATE TABLE IF NOT EXISTS $table_player_stats (uuid VARCHAR(36), name VARCHAR(20), kills int, deaths int, coins int, lang VARCHAR(6), last_login bigint, UNIQUE (uuid))";
            if(!executeUpdate(stmt)) {
                Warrior.getPluginLogger().error(InternalMessages.SQL_GENERAL_ERROR);
            }
        }
    }

    public UserStats getUserData(UUID uuid) {
        if(!isOpen()) return null;

        int pKills = 0;
        int pDeaths = 0;
        int pCoins = 0;

        long pLogin = 0L;

        LanguageCode languageCode = LanguageCode.en_US;
        String stmt = "SELECT * FROM $table_player_stats WHERE uuid = '" + uuid + "';";
        try {
            ResultSet rs = executeQuery(stmt);
            if(rs.next()) {
                pKills = rs.getInt("kills");
                pDeaths = rs.getInt("deaths");
                pCoins = rs.getInt("coins");

                pLogin = rs.getLong("last_login");

                languageCode = LanguageCode.valueOf(rs.getString("lang"));
            }

            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new UserStats(uuid, pKills, pDeaths, pCoins, languageCode, pLogin);
    }

    public UserStats getUserData(String username) {
        if(!isOpen()) return null;

        int pKills = 0;
        int pDeaths = 0;
        int pCoins = 0;

        UUID pUUID = null;

        long pLogin = 0L;

        LanguageCode languageCode = LanguageCode.en_US;
        String stmt = "SELECT * FROM $table_player_stats WHERE name = '" + username + "';";
        try {
            ResultSet rs = executeQuery(stmt);
            if(rs.next()) {
                pUUID = UUID.fromString(rs.getString("uuid"));
                pKills = rs.getInt("kills");
                pDeaths = rs.getInt("deaths");
                pCoins = rs.getInt("coins");

                pLogin = rs.getLong("last_login");

                languageCode = LanguageCode.valueOf(rs.getString("lang"));
            }

            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new UserStats(pUUID, pKills, pDeaths, pCoins, languageCode, pLogin);
    }

}
