package fr.leroideskiwis.bedcraft.menus;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuManager {

    private List<Menu> menus = new ArrayList<>();

    public MenuManager(){
        menus.add(new ShopMenu());
    }

    public Optional<Menu> getMenu(Inventory inventory, CustomPlayer customPlayer){
        return menus.stream().filter(menu -> menu.title(customPlayer).equals(inventory.getTitle())).findAny();
    }

    public Optional<Menu> getMenu(String name){
        return menus.stream().filter(menu -> menu.name().equals(name)).findAny();
    }

}
