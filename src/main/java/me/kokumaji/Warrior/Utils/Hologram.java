package me.kokumaji.Warrior.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.kokumaji.Warrior.Warrior;
import me.kokumaji.HibiscusAPI.api.nms.BukkitHandler;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Hologram {

    private transient Location location;

    // JSON DATA
    @Getter
    private UUID uuid;

    private String[] lines;

    private String world;
    private double locX;
    private double locY;
    private double locZ;

    public Hologram(Location loc, String... text) {
        this.location = loc.add(0, text.length * 0.25, 0);
        this.lines = text;

        this.uuid = UUID.randomUUID();

        this.world = loc.getWorld().getName();
        this.locX = loc.getX();
        this.locY = loc.getY();
        this.locZ = loc.getZ();
    }

    public void save() {
        File f = new File(Warrior.getPlugin().getDataFolder() + "/data/holograms/" + uuid.toString() + ".json");
        f.getParentFile().mkdirs();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try(FileWriter writer = new FileWriter(new File(Warrior.getPlugin().getDataFolder() + "/data/holograms/" + uuid.toString() + ".json"))) {
            gson.toJson(this, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void spawn(Player... players) {
        Translator t = Warrior.getTranslator();

        for(Player p : players) {

            try {
                for(String s : lines) {
                    Object nmsWorld = location.getWorld().getClass().getMethod("getHandle").invoke(location.getWorld());
                    Object entityArmorStand = BukkitHandler.getNMSClass("EntityArmorStand")
                            .getDeclaredConstructor(BukkitHandler.getNMSClass("World"), Double.TYPE, Double.TYPE, Double.TYPE)
                            .newInstance(nmsWorld, location.getX(), location.getY(), location.getZ());
                    location.subtract(0, 0.25, 0);

                    Class<?> asClass = entityArmorStand.getClass();

                    Object chatComponentText = BukkitHandler.getNMSClass("ChatComponentText")
                            .getDeclaredConstructor(String.class)
                            .newInstance(t.parsePlaceholder(p, s));

                    asClass.getMethod("setCustomName", BukkitHandler.getNMSClass("IChatBaseComponent")).invoke(entityArmorStand, chatComponentText);
                    asClass.getMethod("setCustomNameVisible", Boolean.TYPE).invoke(entityArmorStand, true);
                    asClass.getMethod("setInvisible", Boolean.TYPE).invoke(entityArmorStand, true);
                    asClass.getMethod("setSmall", Boolean.TYPE).invoke(entityArmorStand, true);
                    asClass.getMethod("setBasePlate", Boolean.TYPE).invoke(entityArmorStand, false);
                    asClass.getMethod("setNoGravity", Boolean.TYPE).invoke(entityArmorStand, true);

                    Class<?> entity = BukkitHandler.getNMSClass("Entity");
                    Object entitySpawnPacket = BukkitHandler.getNMSClass("PacketPlayOutSpawnEntity").getDeclaredConstructor(entity).newInstance(entityArmorStand);

                    Object metadataPacket = BukkitHandler.getNMSClass("PacketPlayOutEntityMetadata")
                            .getDeclaredConstructor(Integer.TYPE, BukkitHandler.getNMSClass("DataWatcher"), Boolean.TYPE)
                            .newInstance((int)asClass.getMethod("getId").invoke(entityArmorStand), asClass.getMethod("getDataWatcher").invoke(entityArmorStand), false);

                    BukkitHandler.sendPacket(p, entitySpawnPacket);
                    BukkitHandler.sendPacket(p, metadataPacket);
                }

            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }

    }
}
