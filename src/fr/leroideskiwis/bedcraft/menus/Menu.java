package fr.leroideskiwis.bedcraft.menus;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.shop.Shop;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Menu {

    String name();
    String title(CustomPlayer player);
    void open(CustomPlayer player, Shop shop);
    void onClick(ClickType click, Inventory inventory, Shop shop, ItemStack itemStack, int index, CustomPlayer customPlayer);

}
