package fr.leroideskiwis.bedcraft.commands;

import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.shop.ShopItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class RegisterCommand implements CommandExecutor {

    private Shop shop;

    public RegisterCommand(Shop shop){
        this.shop = shop;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        try {

            Material material = Material.valueOf(strings[0].toUpperCase());
            byte data = Byte.parseByte(strings[1]);
            int price = Integer.parseInt(strings[2]);
            int amount = Integer.parseInt(strings[3]);
            shop.add(new ShopItem(new ItemStack(material, amount, data), price, amount));
            commandSender.sendMessage("§aBlock register successfully !");
            shop.save();
            shop.load();

        }catch(Exception exception){
            exception.printStackTrace();
            commandSender.sendMessage("§cSyntaxe : /registerblock <material> <data> <price> <amount>");
        }

        return false;
    }
}
