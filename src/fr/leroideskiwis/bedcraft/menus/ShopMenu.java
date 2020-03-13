package fr.leroideskiwis.bedcraft.menus;

import fr.leroideskiwis.bedcraft.builders.ItemBuilder;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.shop.ShopInventory;
import fr.leroideskiwis.bedcraft.shop.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ShopMenu implements Menu {
    @Override
    public String name() {
        return "shop";
    }

    @Override
    public String title(CustomPlayer player) {
        return "§eBoutique";
    }

    @Override
    public void open(CustomPlayer player, Shop shop) {

        Inventory inventory = Bukkit.createInventory(null, 9*6, title(player));

        for(int i = 0; i < shop.shopItems.size(); i++){
            ShopItem shopItem = shop.shopItems.get(i);
            ItemStack itemStack = shopItem.itemStack.clone();

            Optional<ShopItem> itemPlayer = player.shopInventory.getItem(itemStack);

            inventory.setItem(i, new ItemBuilder(itemStack)
                    .name("§7"+itemStack.getType().toString().toLowerCase())
                    .addLore("§ePrix : "+shopItem.price)
                    .addLore("§eVous possédez "+(itemPlayer.map(item -> "§a" + item.getAmount()).orElse("§c0"))+" item(s)")
                    .addLore("§aClique gauche pour en acheter 1")
                    .addLore("§aClique du milieu pour en acheter 5 (prix: "+shopItem.price*5+")")
                    .addLore("§aClique droit pour en vendre 1")
                    .build());
        }

        player.player.openInventory(inventory);

    }

    @Override
    public void onClick(ClickType click, Inventory inventory, Shop shop, ItemStack itemStack, int index, CustomPlayer customPlayer) {
        ShopInventory shopInventory = customPlayer.shopInventory;
        switch(click){
            case LEFT:
                Optional<ShopItem> shopItem = shop.getItem(itemStack);
                shopItem.ifPresent(shopInventory::buy);
                break;
            case RIGHT:
                Optional<ShopItem> shopItem1 = shopInventory.getItem(itemStack);
                shopItem1.ifPresent(shopInventory::sell);
                break;
            case MIDDLE:
                Optional<ShopItem> shopItem2 = shop.getItem(itemStack);
                for(int i = 0; i < 5; i++) {
                    shopItem2.ifPresent(shopInventory::buy);
                }
                break;
        }

    }
}
