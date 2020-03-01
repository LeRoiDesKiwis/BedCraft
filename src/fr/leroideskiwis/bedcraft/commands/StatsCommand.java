package fr.leroideskiwis.bedcraft.commands;

import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class StatsCommand implements CommandExecutor {
    private final CustomPlayerManager customPlayerManager;

    public StatsCommand(CustomPlayerManager customPlayerManager) {
        this.customPlayerManager = customPlayerManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = null;
        if(strings.length >= 1){
            player = Bukkit.getPlayer(strings[0]);
        }

        if(player == null){
            if(commandSender instanceof Player){
                player = (Player)commandSender;
            } else return true;
        }

        Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
        if(customPlayerOpt.isPresent()){
            CustomPlayer customPlayer = customPlayerOpt.get();
            commandSender.sendMessage("§a----- STATISTIQUES -----");
            commandSender.sendMessage("§eArgent : §6"+customPlayer.getGold());
            commandSender.sendMessage("§1Exp : §3"+customPlayer.getExp());
            commandSender.sendMessage("§9Level : §b"+customPlayer.getLevel());
            commandSender.sendMessage("§a------------------------");
        }


        return false;
    }
}
