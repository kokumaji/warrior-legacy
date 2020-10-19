package com.kokumaji.Warrior.Utils;

import com.kokumaji.Warrior.Warrior;
import me.kokumaji.HibiscusAPI.api.nms.BukkitHandler;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Hologram {

    private Location location;
    private String[] lines;

    public Hologram(Location loc, String... text) {
        this.location = loc.add(0, text.length * 0.25, 0);
        this.lines = text;
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
