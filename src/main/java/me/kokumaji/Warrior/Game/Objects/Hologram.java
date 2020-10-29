package me.kokumaji.Warrior.Game.Objects;

import lombok.Getter;
import me.kokumaji.HibiscusAPI.HibiscusAPI;
import me.kokumaji.HibiscusAPI.api.nms.BukkitHandler;
import me.kokumaji.HibiscusAPI.api.storage.cache.Cacheable;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import me.kokumaji.HibiscusAPI.api.util.MathUtil;
import me.kokumaji.Warrior.Warrior;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Hologram implements Cacheable {

    private transient Location location;

    // JSON DATA
    @Getter
    private final UUID uuid;

    @Getter
    private final String name;

    private final String[] lines;

    private String world;
    private double locX;
    private double locY;
    private double locZ;

    public Hologram(String name, UUID uuid, Location loc, String... text) {
        if(name == null) this.name = MathUtil.generateId();
        else this.name = name;

        this.location = loc.add(0, text.length * 0.25, 0);
        this.lines = text;

        if(uuid == null) this.uuid = UUID.randomUUID();
        else this.uuid = uuid;

        this.world = loc.getWorld().getName();
        this.locX = loc.getX();
        this.locY = loc.getY();
        this.locZ = loc.getZ();
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

    @Override
    public UUID getKey() {
        return uuid;
    }
}
