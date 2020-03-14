package fr.leroideskiwis.bedcraft.listeners;

import fr.leroideskiwis.bedcraft.duel.Duels;
import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.DuelPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

public class DamageEvents implements Listener {
    private final CustomPlayerManager customPlayerManager;
    private final Duels duels;

    public DamageEvents(CustomPlayerManager customPlayerManager, Duels duels) {
        this.customPlayerManager = customPlayerManager;
        this.duels = duels;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player)event.getEntity();

        Optional<CustomPlayer> customPlayerOptional = customPlayerManager.getCustomPlayer(player);
        if(!customPlayerOptional.isPresent()) return;

        CustomPlayer customPlayer = customPlayerOptional.get();

        Optional<DuelPlayer> duelPlayerOpt = duels.getDuelPlayer(customPlayer);

        if(!duelPlayerOpt.isPresent()) return;
        DuelPlayer duelPlayer = duelPlayerOpt.get();

        if(player.getHealth()-event.getFinalDamage() > 0) return;

        if(!duelPlayer.hasBed()) {
            duels.kill(duelPlayer);
            return;
        }

        duels.respawn(duelPlayer);
        event.setCancelled(true);
        player.setHealth(20f);
    }
}
