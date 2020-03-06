package fr.leroideskiwis.bedcraft.builders;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class TextComponentBuilder {

    private final TextComponent textComponent;

    public TextComponentBuilder(String text){
        this.textComponent = new TextComponent(text);
    }

    public TextComponentBuilder setClickEvent(ClickEvent.Action action, String value){
        textComponent.setClickEvent(new ClickEvent(action, value));
        return this;
    }

    public TextComponentBuilder setHoverEvent(HoverEvent.Action action, String value){
        textComponent.setHoverEvent(new HoverEvent(action, new ComponentBuilder(value).create()));
        return this;
    }

    public TextComponent build(){
        return textComponent;
    }
    
}
