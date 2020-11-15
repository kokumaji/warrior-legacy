package me.kokumaji.Warrior.Game.Objects.GUIs;

import java.util.HashMap;
import java.util.Map;

import me.kokumaji.Warrior.Warrior;
import me.kokumaji.Warrior.Game.Objects.GUI;

public class GUIHandler {

    private static Map<String, GUI> guiMap = new HashMap<String, GUI>();

    public static void RegisterGUIs() {

        guiMap.put("arena", new ArenaGUI(45, Warrior.getPlugin()));
        guiMap.put("class", new ClassGUI(36, Warrior.getPlugin()));
    }

    public static GUI GetGUI(String name) {
        if(guiMap.get(name) != null) 
            return guiMap.get(name);

        return null;
    }
    
}
