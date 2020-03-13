package fr.leroideskiwis.bedcraft.modes;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class IdleMode implements PlayerMode {
    @Override
    public void init(CustomPlayer customPlayer) {
        Player player = customPlayer.player;
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setAllowFlight(false);
        customPlayer.teleportToSpawn();
        customPlayer.player.getInventory().clear();
        try {
            customPlayer.base.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
