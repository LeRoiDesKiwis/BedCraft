package fr.leroideskiwis.bedcraft.utils;

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
            int amount = itemStack.getAmount();
            int toSkip = 0;
            for(int i = 0; i < amount; i+=64){
                ItemStack newItem = itemStack.clone();
                newItem.setAmount(64);
                inventory.setItem(index+i, newItem);
                toSkip+=1;
            }
            ItemStack newItem = itemStack.clone();
            newItem.setAmount(amount%64);
            return toSkip;
        }
        return 0;
    }

}
