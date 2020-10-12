package com.kokumaji.Warrior.Game.Managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.kokumaji.Warrior.Game.Objects.WarriorUser;
import org.bukkit.entity.Player;

import com.kokumaji.Warrior.Game.Objects.Arena;

public class UserManager {

    private static Map<UUID, WarriorUser> playerMap = new HashMap<UUID, WarriorUser>();
    private static final Set<Player> playersWithKit = new HashSet<>();
    private static WarriorUser champ = null;

    public static void RemovePlayer(UUID uniqueId) {
        if(playerMap.containsKey(uniqueId)) {
            playerMap.remove(uniqueId);
        }
    }

    public static void AddPlayer(WarriorUser user) {
        if(!playerMap.containsKey(user.getUUID())) {
            playerMap.put(user.getUUID(), user);
        }
    }

    public static boolean IsCached(UUID uniqueId) {
        if(playerMap.containsKey(uniqueId)) return true;
        return false;
    }

    public static WarriorUser GetPlayer(UUID uniqueID) {
        if(IsCached(uniqueID)) return playerMap.get(uniqueID);
        return null;
    }

    public static Collection<WarriorUser> GetPlayers() {
        return playerMap.values();
    }

    public static Set<WarriorUser> GetPlayers(Arena arena) {
        Set<WarriorUser> users = new HashSet<WarriorUser>();
        for(WarriorUser user : playerMap.values()) {
            if(user.getArena() == arena) users.add(user);
        }
        return users;
    }

    public static WarriorUser GetChampion() {
        return champ;
    }

    public static void SetChampion(WarriorUser user) {
        if(champ == null) champ = user;
        if(champ.getStreak() < user.getStreak()) champ = user;
    }

    public static void ClearChampion() {
        champ = null;
    }

}
