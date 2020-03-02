package fr.leroideskiwis.bedcraft.listeners;

import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.SQLException;
import java.util.Optional;

public class CreationEvents implements Listener {

    private CustomPlayerManager customPlayerManager;

    public CreationEvents(CustomPlayerManager customPlayerManager) {
        this.customPlayerManager = customPlayerManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player)event.getEntity();
            Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
            customPlayerOpt.filter(customPlayer -> customPlayer.isState(PlayerState.CREATION)).ifPresent(customPlayer -> event.setCancelled(true));
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) throws SQLException {
        Player player = event.getPlayer();
        Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
        if(customPlayerOpt.isPresent()){
            CustomPlayer customPlayer = customPlayerOpt.get();

            if(customPlayer.isState(PlayerState.CREATION)){
                if((!customPlayer.isIn(event.getBlock()))) {
                    player.sendMessage("§cVous construisez en dehors de votre zone !");
                    event.setCancelled(true);
                } else customPlayer.placeBlock(event.getBlock().getLocation());
            };
        }
    }

    @EventHandler
    public void onBlockDestroyed(PlayerInteractEvent event) throws SQLException {
        if(event.hasBlock() && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
            if (customPlayerOpt.isPresent()) {
                CustomPlayer customPlayer = customPlayerOpt.get();

                if (customPlayer.isState(PlayerState.CREATION)) {
                    if (!customPlayer.isIn(event.getClickedBlock())) {
                        player.sendMessage("§cVous construisez en dehors de votre zone !");
                    } else customPlayer.destroyBlock(event.getClickedBlock());
                }
            }
        }
    }
}
