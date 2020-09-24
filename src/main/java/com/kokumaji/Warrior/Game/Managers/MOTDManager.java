package com.kokumaji.Warrior.Game.Managers;

import com.kokumaji.Warrior.Game.Objects.User;
import com.kokumaji.Warrior.Warrior;
import com.kokumaji.Warrior.Utils.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;

public class MOTDManager {

    File motdFile;
    String[] motdLines;

    public MOTDManager(FileConfiguration conf, Warrior self) {
        motdFile = new File(self.getDataFolder() + "/motd.txt");
        if(!motdFile.exists()) {
            self.saveResource("motd.txt", false);
        }

        this.motdLines = ReadMOTD();
    }

    private String[] ReadMOTD() {
        String[] t = null;
        try(BufferedReader br = new BufferedReader(new FileReader(this.motdFile))) {
            t = br.lines().toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }
    
    public void ReloadMOTD() {
        try(BufferedReader br = new BufferedReader(new FileReader(motdFile))) {
            this.motdLines = br.lines().toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendMOTD(User user) {
        for(String s : motdLines) {
            if(s.startsWith("{CENTER}")) {
                MessageUtil.CenterMessage(user.Bukkit(), s.replace("{CENTER}", ""));
            } else {
                user.SendMessage(s);
            }
        }
    }
}
