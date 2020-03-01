package fr.leroideskiwis.bedcraft.listeners;

import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.menus.Menu;
import fr.leroideskiwis.bedcraft.menus.MenuManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.util.Optional;

public class MenuEvents implements Listener {
    private final MenuManager menuManager;
    private final CustomPlayerManager customPlayerManager;
    private Shop shop;

    public MenuEvents(CustomPlayerManager customPlayerManager, MenuManager menuManager, Shop shop) {
        this.customPlayerManager = customPlayerManager;
        this.menuManager = menuManager;
        this.shop = shop;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event){

        Optional<CustomPlayer> customPlayer = customPlayerManager.getCustomPlayer((Player)event.getWhoClicked());

        if(!customPlayer.isPresent()) return;

        Optional<Menu> menuOpt = menuManager.getMenu(event.getInventory(), customPlayer.get());

        if(!menuOpt.isPresent()) return;

        Menu menu = menuOpt.get();
        menu.onClick(event.getInventory(), shop, event.getCurrentItem(), event.getSlot(), customPlayer.get());

    }

}
