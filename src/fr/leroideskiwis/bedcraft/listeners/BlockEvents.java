package fr.leroideskiwis.bedcraft.listeners;

import fr.leroideskiwis.bedcraft.duel.Duels;
import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.DuelPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BlockEvents implements Listener {

    private final Duels duels;
    private CustomPlayerManager customPlayerManager;
    private Shop shop;

    public BlockEvents(CustomPlayerManager customPlayerManager, Shop shop, Duels duels) {
        this.customPlayerManager = customPlayerManager;
        this.shop = shop;
        this.duels = duels;
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
        if(!customPlayerOpt.isPresent()) return;

        CustomPlayer customPlayer = customPlayerOpt.get();

        Optional<DuelPlayer> duelPlayerOpt = duels.getDuelPlayer(customPlayer);

        Block block = event.getBlock();
        switch (customPlayer.getState()) {
            case CREATION:
                if (customPlayer.isIn(block)) {
                    customPlayer.placeBlock(block.getLocation());
                    return;
                }

                if (!shop.has(Utils.blockToItemStack(block))) {
                    customPlayer.sendMessage("§cCe bloc n'est pas enregistré !");
                    event.setCancelled(true);
                    return;
                }

                player.sendMessage("§cVous construisez en dehors de votre zone !");
                event.setCancelled(true);
                break;
                case DUEL:
                    if(!duelPlayerOpt.isPresent()) return;

                    DuelPlayer duelPlayer = duelPlayerOpt.get();
                    duelPlayer.addLocation(block.getLocation());
                    break;

            default:
                break;

        }
    }

    @EventHandler
    public void onBlockDestroyed(PlayerInteractEvent event) throws SQLException {
        if (event.hasBlock() && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Optional<CustomPlayer> customPlayerOpt = customPlayerManager.getCustomPlayer(player);
            if (!customPlayerOpt.isPresent()) return;

            CustomPlayer customPlayer = customPlayerOpt.get();

            Block block = event.getClickedBlock();
            switch (customPlayer.getState()) {
                case CREATION:
                    if (customPlayer.isIn(block)) {
                        customPlayer.destroyBlock(block);
                        return;
                    }

                    player.sendMessage("§cVous construisez en dehors de votre zone !");
                    event.setCancelled(true);
                    break;
                case DUEL:
                    Optional<DuelPlayer> duelPlayerOpt = duels.getDuelPlayer(customPlayer);
                    if(!duelPlayerOpt.isPresent()) return;

                    DuelPlayer duelPlayer = duelPlayerOpt.get();
                    duelPlayer.removeLocation(block.getLocation());
                    break;
                default:
                    break;
            }
        }
    }
}
