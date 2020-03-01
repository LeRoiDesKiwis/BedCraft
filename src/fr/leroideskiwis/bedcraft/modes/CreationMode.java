package fr.leroideskiwis.bedcraft.modes;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CreationMode implements PlayerMode {

    @Override
    public void init(CustomPlayer customPlayer) throws SQLException {
        Player player = customPlayer.player;
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(true);
        customPlayer.base.teleport();
        customPlayer.shopInventory.setInventoryPlayer();
    }

}
