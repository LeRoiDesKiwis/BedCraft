package fr.leroideskiwis.bedcraft.shop;

import fr.leroideskiwis.bedcraft.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Shop {

    public final SQLManager sqlManager;
    public final List<ShopItem> shopItems = new ArrayList<>();

    public Shop(SQLManager sqlManager){
        this.sqlManager = sqlManager;
    }

    public void load() throws SQLException {
        ResultSet resultSet = sqlManager.executeQuery("SELECT * FROM shopItems");
        System.out.println("Loading shop...");
        while(resultSet.next()){
            System.out.println(extractResult(resultSet).itemStack.toString());
            shopItems.add(extractResult(resultSet));
        }
    }

    public void save() throws SQLException {
        PreparedStatement preparedStatement = sqlManager.prepareStatement("INSERT INTO shopItems (material, data, price, amount) " +
                "VALUES (?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE" +
                " material=VALUES(material), " +
                "data=VALUES(data), " +
                "price=VALUES(price)"+
                "amount=VALUES(amount)");
        PreparedStatement preparedStatement1 = sqlManager.prepareStatement("SELECT * FROM shopItems WHERE id=?");
        for (ShopItem shopItem : shopItems) {
            shopItem.completeStatement(preparedStatement1, 1);
            if(sqlManager.hasNext(preparedStatement1)) continue;

            preparedStatement.setString(1, shopItem.itemStack.getType().toString());
            preparedStatement.setByte(2, shopItem.itemStack.getData().getData());
            preparedStatement.setInt(3, shopItem.price);
            preparedStatement.setInt(4, shopItem.getAmount());
            preparedStatement.execute();

        }
        preparedStatement1.close();
        preparedStatement.close();

    }

    public void add(ShopItem shopItem){
        shopItems.remove(shopItem);
        shopItems.add(shopItem);
    }

    public Optional<ShopItem> getItem(ItemStack itemStack){
        if(itemStack == null) return Optional.empty();
        return shopItems.stream().filter(shopItem -> shopItem.itemStack.getType() == itemStack.getType()).findAny();
    }

    public boolean has(ItemStack itemStack){
        return getItem(itemStack).isPresent();
    }

    private ShopItem extractResult(ResultSet resultSet) throws SQLException {
        return of(resultSet.getString("material"), resultSet.getByte("data"), resultSet.getInt("price"), resultSet.getInt("id"), resultSet.getInt("amount"));
    }

    public ShopItem getInDatabase(ResultSet resultSet) throws SQLException {
        PreparedStatement preparedStatement = sqlManager.prepareStatement("SELECT * FROM shopItems WHERE id=?");
        preparedStatement.setInt(1, resultSet.getInt("itemId"));
        ResultSet resultSet1 = preparedStatement.executeQuery();

        if(!resultSet1.next()) return null;

        ShopItem shopItem = extractResult(resultSet1);
        resultSet1.close();
        return shopItem;
    }

    public ShopItem of(String material, byte data, int price, int id, int amount) {
        return new ShopItem(new ItemStack(Material.valueOf(material), amount, data), price, id, amount);

    }

}
