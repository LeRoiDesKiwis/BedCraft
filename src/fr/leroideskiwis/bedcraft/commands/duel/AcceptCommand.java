package fr.leroideskiwis.bedcraft.commands.duel;

import fr.leroideskiwis.bedcraft.duel.Duels;
import fr.leroideskiwis.bedcraft.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand implements CommandExecutor {

    private Duels duels;

    public AcceptCommand(Duels duels){
        this.duels = duels;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!CommandUtils.isPlayer(commandSender)) return false;
        Player player = (Player) commandSender;

        if(!duels.hasRequest(player)){
            player.sendMessage("§cVous n'avez pas de requête à accepter !");
            return false;
        }

        duels.accept(player);

        return false;
    }
}
