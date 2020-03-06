package fr.leroideskiwis.bedcraft.shop;

import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.menus.MenuManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ShopCommand implements CommandExecutor {
    private final CustomPlayerManager customPlayerManager;
    private final Shop shop;
    private final MenuManager menuManager;

    public ShopCommand(CustomPlayerManager customPlayerManager, Shop shop, MenuManager menuManager) {
        this.customPlayerManager = customPlayerManager;
        this.shop = shop;
        this.menuManager = menuManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(CommandUtils.isPlayer(commandSender)){
            Player player = (Player)commandSender;
            Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
            customPlayerOpt.ifPresent(customPlayer -> menuManager.getMenu("shop").ifPresent(menu -> menu.open(customPlayer, shop)));
        }
        return false;
    }
}
