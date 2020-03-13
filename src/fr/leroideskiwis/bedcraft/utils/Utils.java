package fr.leroideskiwis.bedcraft.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static ItemStack blockToItemStack(Block block){
        return new ItemStack(block.getType(), 1, (short)0, block.getData());
    }

    public static int setInventory(Inventory inventory, int index, ItemStack itemStack){
        if(itemStack.getAmount() <= 64) inventory.setItem(index, itemStack);
        else {
            int amount = itemStack.getAmount()/64;
            int toSkip = 0;
            for(int i = 0; i < amount && index+i < 36; i++){
                ItemStack newItem = itemStack.clone();
                newItem.setAmount(64);
                inventory.setItem(index+i, newItem);
                toSkip++;
            }
            ItemStack newItem = itemStack.clone();
            newItem.setAmount(amount%64);
            inventory.setItem(index+toSkip, newItem);
            return toSkip+1;
        }
        return 1;
    }

    public static boolean isInt(String text){
        return text.matches("^-?\\d+$");
    }

    public static Location add(Location location, double x, double y, double z){
        return new Location(location.getWorld(), location.getX()+x, location.getY()+y, location.getZ()+z);
    }

    public static Location add(Location location, Location loc) {
        return add(location, loc.getX(), loc.getY(), loc.getZ());
    }

    public static Location remove(Location location, Location loc){
        return add(location, -loc.getX(), -loc.getY(), -loc.getZ());
    }
}
