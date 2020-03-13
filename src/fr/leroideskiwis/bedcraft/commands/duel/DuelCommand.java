package fr.leroideskiwis.bedcraft.commands.duel;

import fr.leroideskiwis.bedcraft.duel.Duels;
import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import fr.leroideskiwis.bedcraft.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DuelCommand implements CommandExecutor {
    private final CustomPlayerManager customPlayerManager;
    private Duels duels;

    public DuelCommand(CustomPlayerManager customPlayerManager, Duels duels) {
        this.customPlayerManager = customPlayerManager;
        this.duels = duels;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!CommandUtils.isPlayer(commandSender)) return false;
        Player player = (Player) commandSender;

        if (strings.length == 0) {
            player.sendMessage("§cVous devez spécifier un joueur !");
            return false;
        }

        Player target = Bukkit.getPlayer(strings[0]);
        if (target == null) {
            player.sendMessage("§cLe joueur n'existe pas !");
            return false;
        }

        /*if(target.equals(player)){
            player.sendMessage("§cVous ne pouvez pas vous défier vous même !");
            return false;
        }*/

        if (duels.hasRequest(player)) {
            player.sendMessage("§cVous avez déjà un duel en attente");
            return false;
        }

        if (duels.hasRequest(target)) {
            player.sendMessage("§cLa cible a déjà un duel en attente.");
            return false;
        }

        if(duels.isInDuel(player)){
            player.sendMessage("§cVous êtes déjà en duel !");
            return false;
        }

        if(duels.isInDuel(target)){
            player.sendMessage("§c"+target.getDisplayName()+" est déjà en duel !");
            return false;
        }

        duels.request(player, target);
        return false;
    }
}
