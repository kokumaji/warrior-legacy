package com.kokumaji.StickyPvP.Utils;

import com.kokumaji.StickyPvP.StickyPvP;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class NMSUtil {

    private ArrayList<String> a = new ArrayList<String>();
    private StickyPvP self = (StickyPvP) StickyPvP.GetPlugin();
    private String v;

    public NMSUtil() {
        loadVersions();
        String clazz = Bukkit.getServer().getClass().getPackage().getName();
        v = clazz.substring(clazz.lastIndexOf('.') + 1);

        if (!a.contains(v)) {
            InternalMessages.NMS_UNSUPPORTED.Log(v);
        }
    }

    public Class<?> NMSClass(String pkg) {
        if (pkg.contains("$version")) {
            String nmsPkg = pkg.replace("$version", v);
            try {
                return Class.forName(nmsPkg);
            } catch (ClassNotFoundException e) {
                InternalMessages.NMS_CLASS_NOT_FOUND.Log(nmsPkg);
                e.printStackTrace();
            }
        }

        return null;
    }

    private void loadVersions() {
        a.add("v1_15_R1");
        a.add("v1_16_R1");
    }
}
