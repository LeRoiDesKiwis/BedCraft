package fr.leroideskiwis.bedcraft.utils;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static ItemStack blockToItemStack(Block block){
        return new ItemStack(block.getType(), 1, (short)0, block.getData());
    }

}
