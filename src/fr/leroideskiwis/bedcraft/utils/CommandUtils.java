package fr.leroideskiwis.bedcraft.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtils {

    public static boolean isPlayer(CommandSender commandSender){
        if(commandSender instanceof Player) return true;
        else {
            commandSender.sendMessage("§cErreur : vous devez être un joueur pour exécuter cette commande !");
            return false;
        }
    }

}
