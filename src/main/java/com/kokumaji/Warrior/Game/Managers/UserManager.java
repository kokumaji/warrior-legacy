package com.kokumaji.Warrior.Game.Managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;

import com.kokumaji.Warrior.Game.Objects.Arena;
import com.kokumaji.Warrior.Game.Objects.User;

public class UserManager {

    private static Map<UUID, User> playerMap = new HashMap<UUID, User>();
    private static final Set<Player> playersWithKit = new HashSet<>();
    private static User champ = null;

    public static void RemovePlayer(UUID uniqueId) {
        if(playerMap.containsKey(uniqueId)) {
            playerMap.remove(uniqueId);
        }
    }

    public static void AddPlayer(User User) {
        if(!playerMap.containsKey(User.GetUUID())) {
            playerMap.put(User.GetUUID(), User);
        }
    }

    public static boolean IsCached(UUID uniqueId) {
        if(playerMap.containsKey(uniqueId)) return true;
        return false;
    }

    public static User GetPlayer(UUID uniqueID) {
        if(IsCached(uniqueID)) return playerMap.get(uniqueID);
        return null;
    }

    public static Collection<User> GetPlayers() {
        return playerMap.values();
    }

    public static Set<User> GetPlayers(Arena arena) {
        Set<User> users = new HashSet<User>();
        for(User user : playerMap.values()) {
            if(user.GetArena() == arena) users.add(user);
        }
        return users;
    }

    public static User GetChampion() {
        return champ;
    }

    public static void SetChampion(User user) {
        if(champ == null) champ = user;
        if(champ.GetStreak() < user.GetStreak()) champ = user;
    }

    public static void ClearChampion() {
        champ = null;
    }

}
