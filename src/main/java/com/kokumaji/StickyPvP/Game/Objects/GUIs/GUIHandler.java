package com.kokumaji.StickyPvP.Game.Objects.GUIs;

import java.util.HashMap;
import java.util.Map;

import com.kokumaji.StickyPvP.StickyPvP;
import com.kokumaji.StickyPvP.Game.Objects.GUI;

public class GUIHandler {

    private static Map<String, GUI> guiMap = new HashMap<String, GUI>(); 

    public static void RegisterGUIs() {
        guiMap.put("arena", new ArenaGUI(45, StickyPvP.GetPlugin()));
    }

    public static GUI GetGUI(String name) {
        if(guiMap.get(name) != null) 
            return guiMap.get(name);

        return null;
    }
    
}
