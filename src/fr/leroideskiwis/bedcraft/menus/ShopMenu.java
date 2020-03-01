package fr.leroideskiwis.bedcraft.menus;

import fr.leroideskiwis.bedcraft.builders.ItemBuilder;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.shop.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
                    .addLore("§eVous possédez "+(itemPlayer.isPresent() ? "§a"+itemPlayer.get().getAmount() : "§c0")+" items")
                    .build());
        }

        player.player.openInventory(inventory);

    }

    @Override
    public void onClick(Inventory inventory, Shop shop, ItemStack itemStack, int index, CustomPlayer customPlayer) {
        Optional<ShopItem> shopItem = shop.getItem(itemStack);
        if(!shopItem.isPresent()) return;
        customPlayer.shopInventory.buy(shopItem.get());
        customPlayer.player.closeInventory();
    }
}
