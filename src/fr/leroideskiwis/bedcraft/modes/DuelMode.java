package fr.leroideskiwis.bedcraft.modes;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import org.bukkit.GameMode;

public class DuelMode implements PlayerMode {
    @Override
    public void init(CustomPlayer customPlayer) {
        customPlayer.player.setGameMode(GameMode.SURVIVAL);
    }
}
