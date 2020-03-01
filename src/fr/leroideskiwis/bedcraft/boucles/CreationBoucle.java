package fr.leroideskiwis.bedcraft.boucles;

import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class CreationBoucle implements Runnable {
    private final CustomPlayerManager customPlayerManager;

    public CreationBoucle(CustomPlayerManager customPlayerManager) {
        this.customPlayerManager = customPlayerManager;
    }

    @Override
    public void run() {
        for(CustomPlayer customPlayer : customPlayerManager.getCustomPlayers()){
            if(customPlayer.isState(PlayerState.CREATION)){
                try {
//                    Bukkit.broadcastMessage(customPlayer.player.getDisplayName()+" : "+customPlayer.base.isIn());

                    if(!customPlayer.base.isIn()) {
                        customPlayer.base.teleport();
                        customPlayer.player.sendMessage("§cErreur : vous dépassez les limites de votre base !");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    customPlayer.player.sendMessage("Une erreur s'est produite. Veuillez contacter un administrateur.");
                    customPlayer.setPlayerState(PlayerState.IDLE);
                }

            }
        }
    }
}
