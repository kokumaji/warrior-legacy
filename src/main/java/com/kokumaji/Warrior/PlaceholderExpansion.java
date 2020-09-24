package com.kokumaji.Warrior;

import org.bukkit.entity.Player;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private Warrior plugin;

    public PlaceholderExpansion(Warrior pl) {
        this.plugin = pl;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "kitpvp";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        String[] identifierArgs = identifier.split("_");
        // TODO: ADD PLACEHOLDERS
        return null;
    }
}

