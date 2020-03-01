package fr.leroideskiwis.bedcraft.shop;

import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ShopItem implements Cloneable{
    public final int price;
    public final ItemStack itemStack;
    private final int id;

    public ShopItem(ItemStack itemStack, int price, int id, int amount){
        this.price = price;
        itemStack.setAmount(amount);
        this.itemStack = itemStack;
        this.id = id;
    }

    public ShopItem(ItemStack itemStack, int price, int amount){
        this(itemStack, price, -1, amount);
    }

    public void completeStatement(PreparedStatement preparedStatement, int start) throws SQLException {
        preparedStatement.setInt(start, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopItem shopItem = (ShopItem) o;
        return price == shopItem.price &&
                id == shopItem.id &&
                Objects.equals(itemStack, shopItem.itemStack);
    }

    public void add(ShopItem shopItem){
        itemStack.setAmount(itemStack.getAmount()+shopItem.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, itemStack, id);
    }

    @Override
    protected ShopItem clone() {
        return new ShopItem(itemStack, price, id, getAmount());
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    public void setAmount(int amount) {
        itemStack.setAmount(amount);
    }
}
