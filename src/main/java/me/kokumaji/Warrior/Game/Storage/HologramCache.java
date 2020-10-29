package me.kokumaji.Warrior.Game.Storage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.kokumaji.HibiscusAPI.api.storage.cache.Cacheable;
import me.kokumaji.HibiscusAPI.api.storage.cache.PersistentCache;
import me.kokumaji.Warrior.Game.Objects.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.UUID;

public class HologramCache<T extends Cacheable> extends PersistentCache<Hologram> {

    public HologramCache(Plugin owner, Class<Hologram> clazz) {
        super(owner, clazz);
        loadFromDisk();
    }

    public Hologram fromName(String name) {
        for(Hologram h : objects.values()) {
            if(h.getName().equals(name)) return h;
        }

        return null;
    }

    public void modify(Hologram hg) {
        if(!contains(hg.getKey())) return;

        objects.remove(hg.getKey());
        objects.put(hg.getKey(), hg);
    }

    @Override
    public void loadFromDisk() {
        File folder = new File(this.DATA_PATH);
        if(!folder.exists()) return;

        Gson gson = new Gson();

        for(File f : folder.listFiles()) {
            try {
                JsonObject js = gson.fromJson(new FileReader(f), JsonObject.class);

                UUID uuid = UUID.fromString(js.get("uuid").getAsString());
                String[] lines = gson.fromJson(js.getAsJsonArray("lines"), String[].class);
                Location loc = new Location(
                        Bukkit.getWorld(js.get("world").getAsString()),
                        js.get("locX").getAsDouble(),
                        js.get("locY").getAsDouble(),
                        js.get("locZ").getAsDouble()
                );

                String name = js.get("name").getAsString();

                Hologram hg = new Hologram(name, uuid, loc, lines);

                this.add(hg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
