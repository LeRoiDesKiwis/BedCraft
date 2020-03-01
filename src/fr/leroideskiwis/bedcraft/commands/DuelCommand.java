package fr.leroideskiwis.bedcraft.commands;

import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import fr.leroideskiwis.bedcraft.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DuelCommand implements CommandExecutor {
    private final CustomPlayerManager customPlayerManager;

    public DuelCommand(CustomPlayerManager customPlayerManager) {
        this.customPlayerManager = customPlayerManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(CommandUtils.isPlayer(commandSender)){
            Player player = (Player)commandSender;
            Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
            customPlayerOpt.ifPresent(customPlayer -> customPlayer.toggle(PlayerState.IDLE, PlayerState.DUEL));

        }

        return false;
    }
}
