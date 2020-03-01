package fr.leroideskiwis.bedcraft.builders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private List<String> lores = new ArrayList<>();

    public ItemBuilder(Material material){
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack itemStack){
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder name(String name){
        itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder addLore(String string){
        lores.add(string);
        return this;
    }

    public ItemStack build(){
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
