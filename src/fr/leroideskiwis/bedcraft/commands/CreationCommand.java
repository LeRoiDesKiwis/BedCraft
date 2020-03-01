package fr.leroideskiwis.bedcraft.commands;

import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CreationCommand implements CommandExecutor {
    private final CustomPlayerManager customPlayerManager;
    private final Shop shop;

    public CreationCommand(CustomPlayerManager customPlayerManager, Shop shop) {
        this.customPlayerManager = customPlayerManager;
        this.shop = shop;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(CommandUtils.isPlayer(commandSender)){
            Player player = (Player)commandSender;

            Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
            customPlayerOpt.ifPresent(customPlayer -> customPlayer.toggle(PlayerState.IDLE, PlayerState.CREATION));

        }

        return false;
    }
}
