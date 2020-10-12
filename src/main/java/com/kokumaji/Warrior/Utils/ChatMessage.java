package com.kokumaji.Warrior.Utils;

import com.kokumaji.Warrior.Warrior;
import me.kokumaji.HibiscusAPI.api.translation.Translator;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class ChatMessage {

    String c;
    OfflinePlayer p;

    TextComponent header;
    TextComponent tail;

    public ChatMessage(OfflinePlayer player, String content) {
        this.p = player;
        this.c = content;

        header = setHeader();
        tail = setTail();
    }

    private TextComponent setTail() {
        Player pl = (Player) p;
        if(pl.hasPermission("warrior.chat.color"))
            c = Translator.ApplyColor(c);

        return new TextComponent(" §7» §r" + c);
    }

    public TextComponent getChatMessage() {
        TextComponent component = new TextComponent(header, tail);
        return component;
    }

    public TextComponent getTail() {
        return tail;
    }

    protected TextComponent setHeader() {
        TextComponent h = new TextComponent();
        Translator t = Warrior.getTranslator();
        h.setText(p.getName());
        String hoverText = Translator.ApplyColor(playerHover().getText());
        h.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(t.parsePlaceholder(p, hoverText))));

        return h;
    }

    private TextComponent playerHover() {
        TextComponent tooltip = new TextComponent();
        tooltip.setText("&3&l&o{warrior:player}'s Stats\n&8» &7Kills: &f{warrior:kills}\n&8» &7Deaths: &f{warrior:deaths}\n&8» &7KDR: &f{warrior:kdr}");
        return tooltip;
    }
}
