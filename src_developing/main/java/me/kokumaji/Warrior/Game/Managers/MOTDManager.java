package me.kokumaji.Warrior.Game.Managers;

import me.kokumaji.Warrior.Game.Objects.WarriorUser;
import me.kokumaji.Warrior.Warrior;
import me.kokumaji.Warrior.Utils.MessageUtil;
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

    public void SendMOTD(WarriorUser user) {
        for(String s : motdLines) {
            if(s.startsWith("{CENTER}")) {
                MessageUtil.CenterMessage(user.bukkit(), s.replace("{CENTER}", ""));
            } else {
                user.sendMessage(s);
            }
        }
    }
}
