package fr.leroideskiwis.bedcraft.commands;

import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.utils.CommandUtils;
import fr.leroideskiwis.bedcraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Optional;

public class GiveMoneyCommand implements CommandExecutor {

    private final CustomPlayerManager customPlayerManager;

    public GiveMoneyCommand(CustomPlayerManager customPlayerManager){
        this.customPlayerManager = customPlayerManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!CommandUtils.isPlayer(commandSender)) return false;
        Player player = (Player)commandSender;

        if(strings.length < 2){
            player.sendMessage("§cVous devez donner un pseudo et un nombre !");
            return false;
        }

        if(!Utils.isInt(strings[1])){
            player.sendMessage("§cVous devez donner un nombre !");
            return false;
        }

        int transfer = Integer.parseInt(strings[1]);

        Player target = Bukkit.getPlayer(strings[0]);

        if(target == null){
            player.sendMessage("§cCe joueur n'existe pas.");
            return false;
        }

        Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
        Optional<CustomPlayer> customTargetOpt = customPlayerManager.getCustomPlayer(target);

        if(!customPlayerOpt.isPresent() || !customTargetOpt.isPresent()) return false;

        CustomPlayer customPlayer = customPlayerOpt.get();
        CustomPlayer customTarget = customTargetOpt.get();

        if(!player.hasPermission("bedcraft.givemoney")){
            if(!customPlayer.hasEnough(transfer)){
                player.sendMessage("§cVous n'avez pas assez d'argent !");
                return false;
            }

            if(transfer < 1){
                player.sendMessage("§cVous devez mettre un nombre positif !");
                return false;
            }
            customPlayer.removeGold(transfer);
        }

        customTarget.addGold(transfer);
        if(transfer > 0) {
            customTarget.sendMessage("§aVous avez reçu " + transfer + " de la part de " + customPlayer.getName());
            customPlayer.sendMessage("§aVous avez envoyé " + transfer + " à " + customTarget.getName());
        } else {
            customTarget.sendMessage("§c"+customPlayer.getName()+" vous a \"emprunté\" "+-transfer);
            customPlayer.sendMessage("§cVous avez \"emprunté\" " + -transfer + " à " + customTarget.getName());
        }

        return false;
    }
}
