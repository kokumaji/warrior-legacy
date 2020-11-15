package me.kokumaji.Warrior.Utils;

import me.kokumaji.HibiscusAPI.api.util.MathUtil;
import me.kokumaji.Warrior.Warrior;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

public class ProgressBar {

    private Runnable executeAfter;

    public ProgressBar execute(Runnable r) {
        this.executeAfter = r;
        return this;
    }

    public enum BarStyle {
        CLASSIC('|'),
        BOLD('❚');


        char bar;
        BarStyle(char s) {
            this.bar = s;
        }

        char get() {
            return bar;
        }
    }

    private Location location;
    private ArmorStand armorStand;

    private ChatColor fgColor = ChatColor.GREEN;
    private ChatColor bgColor = ChatColor.RED;

    private Sound sound;

    private BarStyle style;

    private long speed;
    private int bars;

    private float maxPitch;

    public ProgressBar(Location loc, int bars, long s, BarStyle style) {
        this.location = loc.subtract(0, 0.75, 0);
        this.style = style;
        this.bars = bars;
        this.speed = s;
    }

    public ProgressBar setForeground(ChatColor color) {
        if(color != null) {
            fgColor = color;
        }

        return this;
    }

    public ProgressBar setBackground(ChatColor color) {
        if(color != null) {
            bgColor = color;
        }

        return this;
    }

    public ProgressBar playSound(Sound sound) {
        if(sound != null) {
            this.sound = sound;
        }

        return this;
    }

    public ProgressBar setMaxPitch(float max) {
        maxPitch = max;

        return this;
    }

    public void spawn() {
        Location finalLoc = location;
        armorStand = (ArmorStand) finalLoc.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);

        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);

        new BukkitRunnable() {
            String progress = fgColor.toString();
            String empty = new String(new char[bars]).replace('\0', style.get());
            int i = 0;

            @Override
            public void run() {
                if(i >= bars) {
                    cancel();
                    armorStand.remove();
                    if(executeAfter != null) {
                        new Thread(executeAfter).start();
                    }
                }

                String percent = MathUtil.percentageAsString(i, bars, 0);

                double pitch = 0.5 * Math.pow(maxPitch * 2, (double)i / (double)bars);

                if(sound != null)
                    finalLoc.getWorld().playSound(finalLoc, sound, 1f, (float)pitch);

                String bar = progress + bgColor + empty.substring(i) + " §f" + percent;
                armorStand.setCustomName(bar);

                progress = progress + style.get();
                i++;
            }
        }.runTaskTimer(Warrior.getPlugin(), 0L, speed);
    }

}
