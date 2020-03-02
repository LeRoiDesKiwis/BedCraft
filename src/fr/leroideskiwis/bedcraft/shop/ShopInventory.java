package fr.leroideskiwis.bedcraft.shop;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import fr.leroideskiwis.bedcraft.sql.SQLManager;
import fr.leroideskiwis.bedcraft.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ShopInventory {

    private final SQLManager sqlManager;
    private final List<ShopItem> items = new ArrayList<>();
    private final CustomPlayer customPlayer;
    private final Shop shop;

    public ShopInventory(CustomPlayer customPlayer, Shop shop, SQLManager sqlManager){
        this.customPlayer = customPlayer;
        this.sqlManager = sqlManager;
        this.shop = shop;
    }

    public void buy(ShopItem shopItem) {
        if(shopItem == null) return;
        if(customPlayer.hasEnough(shopItem.price)) {
            if (has(shopItem.itemStack)) {
                ShopItem shopItem1 = getItem(shopItem.itemStack).get();
                shopItem1.add(shopItem);
            } else {
                items.add(shopItem.clone());
            }
            customPlayer.removeGold(shopItem.price);
            customPlayer.player.sendMessage("§aVous avez acheté " + shopItem.itemStack.getType().toString().toLowerCase() + " pour " + shopItem.price + " coins");
            customPlayer.sendMessage("§aVous avez désormais " + getItem(shopItem.itemStack).get().getAmount() + " " + shopItem.itemStack.getType().toString().toLowerCase());
            if(customPlayer.isState(PlayerState.CREATION)) setInventoryPlayer();
        } else customPlayer.player.sendMessage("§cIl vous manque " + (shopItem.price - customPlayer.getGold()) + " pour acheter ceci !");
    }

    public Optional<ShopItem> getItem(ItemStack item){
        if(item == null) return Optional.empty();
        return items.stream().filter(shopItem2 -> {
            if(shopItem2 != null && shopItem2.itemStack != null) return shopItem2.itemStack.isSimilar(item);
            return false;
        }).findAny();
    }

    public boolean has(ItemStack itemStack){
        return getItem(itemStack).isPresent();
    }

    public boolean has(Material material){
        return has(new ItemStack(material));
    }

    public void save() throws SQLException {
        PreparedStatement preparedStatement = sqlManager.prepareStatement("INSERT INTO inventories (playerId, itemId, amount) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE amount=VALUES(amount)");
        for (ShopItem shopItem : items) {
            if(shopItem == null) continue;
            preparedStatement.setInt(1, customPlayer.getId());
            shopItem.completeStatement(preparedStatement, 2);
            preparedStatement.setInt(3, shopItem.getAmount());
            preparedStatement.execute();
        }
        preparedStatement.close();
    }

    public void load() throws SQLException {
        PreparedStatement preparedStatement = sqlManager.prepareStatement("SELECT * FROM inventories WHERE playerId=?");
        preparedStatement.setInt(1, customPlayer.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ShopItem shopItem = shop.getInDatabase(resultSet);
            if(shopItem == null) continue;
            shopItem.setAmount(resultSet.getInt("amount"));
            items.add(shopItem);
        }
        preparedStatement.close();
        resultSet.close();
    }

    public List<ShopItem> getShopItems(){
        return new ArrayList<>(items);
    }

    public void setInventoryPlayer(){
        customPlayer.player.getInventory().clear();
        for (int i = 0; i < customPlayer.shopInventory.getShopItems().size() && i < 35; i++) {
            ItemStack toAdd = customPlayer.shopInventory.getShopItems().get(i).itemStack.clone();
            int count = customPlayer.base.countBlocks(toAdd);
            toAdd.setAmount(toAdd.getAmount()-count);
            i+= Utils.setInventory(customPlayer.player.getInventory(), i, toAdd);
        }
    }

}
