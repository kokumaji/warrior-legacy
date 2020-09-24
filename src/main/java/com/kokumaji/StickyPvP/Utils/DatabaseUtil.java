package com.kokumaji.StickyPvP.Utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.kokumaji.StickyPvP.Game.Objects.UserStats;
import com.kokumaji.StickyPvP.StickyPvP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseUtil {

    private static HikariDataSource ds;
    private static Plugin self = StickyPvP.GetPlugin();
    private FileConfiguration c;

    public DatabaseUtil(FileConfiguration conf) {
        c = conf;

        String sqlHost = c.getString("data-storage.mysql-settings.host");
        String sqlDB = c.getString("data-storage.mysql-settings.database");
        int sqlPort = c.getInt("data-storage.mysql-settings.port");
        String sqlUser = c.getString("data-storage.mysql-settings.username");
        String sqlPass = c.getString("data-storage.mysql-settings.password");

        HikariConfig config = new HikariConfig();
        
        config.setJdbcUrl("jdbc:mysql://" + sqlHost + ":" + sqlPort + "/" + sqlDB);
        config.setUsername(sqlUser);
        config.setPassword(sqlPass);
        config.setConnectionTimeout(2500);
        config.setMaxLifetime(32);

        if (ds == null) {
            try {
                ds = new HikariDataSource(config);
                InternalMessages.SQL_CONNECT_SUCCESS.Log();
            } catch (Exception err) {
                InternalMessages.SQL_CONNECT_ERROR.Log(err.getMessage());
                err.printStackTrace();
            }
        }
    }


    public boolean ExecuteUpdate(String s) {
        if(ds == null)
            return false;

        try {
            Connection connection = ds.getConnection();
            String tablePrefix = c.getString("data-storage.mysql-settings.table-prefix");
            if(!tablePrefix.endsWith("_"))
                tablePrefix = tablePrefix + "_";

            s = s.replace("$table_", tablePrefix);
            if(connection != null && connection.isValid(3)) {
                PreparedStatement stmt = connection.prepareStatement(s);
                stmt.executeUpdate();
                stmt.close();
                connection.close();
            }

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public void MakeTables() {
        if (ds == null)
            return;

        try {
            Connection conn = ds.getConnection();
            if (conn != null && conn.isValid(5)) {
                String tablePrefix = c.getString("data-storage.mysql-settings.table-prefix");
                if(!tablePrefix.endsWith("_"))
                    tablePrefix = tablePrefix + "_";

                PreparedStatement stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + tablePrefix + "player_stats (uuid VARCHAR(36), kills int, deaths int, coins int, lang VARCHAR(6), UNIQUE (uuid))");
                stmt.executeUpdate();
                
                stmt.close();
                conn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean Execute(String s) {
        if(ds == null)
            return false;

        try {
            Connection connection = ds.getConnection();
            String tablePrefix = c.getString("data-storage.mysql-settings.table-prefix");
            if(!tablePrefix.endsWith("_"))
                tablePrefix = tablePrefix + "_";

            s = s.replace("$table_", tablePrefix);
            if(connection != null && connection.isValid(3)) {
                PreparedStatement stmt = connection.prepareStatement(s);
                stmt.execute();
                stmt.close();
                connection.close();
            }

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public UserStats GetUserData(UUID uuid) {
        if(ds == null)
            return null;

        int pKills = 0;
        int pDeaths = 0;
        int pCoins = 0;
        LanguageCode languageCode = LanguageCode.en_US;

        try {
            Connection conn = ds.getConnection();
            String tablePrefix = c.getString("data-storage.mysql-settings.table-prefix");
            if(!tablePrefix.endsWith("_"))
                tablePrefix = tablePrefix + "_";

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tablePrefix + "player_stats WHERE uuid = '" + uuid + "';");
            ResultSet rs;

            rs  = stmt.executeQuery();
            if(rs.next()) {
                pKills = rs.getInt("kills");
                pDeaths = rs.getInt("deaths");
                pCoins = rs.getInt("coins");
                languageCode = LanguageCode.valueOf(rs.getString("lang"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new UserStats(uuid, pKills, pDeaths, pCoins, languageCode);
    }
}
