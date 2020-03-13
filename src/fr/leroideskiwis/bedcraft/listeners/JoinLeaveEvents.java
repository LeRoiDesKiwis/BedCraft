package fr.leroideskiwis.bedcraft.listeners;

import fr.leroideskiwis.bedcraft.core.Prefixes;
import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.sql.SQLException;

public class JoinLeaveEvents implements Listener {

    private final CustomPlayerManager customPlayerManager;
    private Shop shop;
    private final SQLManager sqlManager;

    public JoinLeaveEvents(CustomPlayerManager customPlayerManager, SQLManager sqlManager, Shop shop) {
        this.customPlayerManager = customPlayerManager;
        this.sqlManager = sqlManager;
        this.shop = shop;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        customPlayerManager.remove(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        customPlayerManager.registerAndLoad(player, shop);
        CustomPlayer customPlayer = customPlayerManager.getCustomPlayer(player).get();

        if(!sqlManager.exists(player.getUniqueId())) {

            customPlayer.addGold(1000);

            Firework firework = (Firework)player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            fireworkMeta.addEffects(FireworkEffect.builder()
                    .withColor(org.bukkit.Color.RED)
                    .withColor(org.bukkit.Color.BLUE)
                    .withFade(Color.GREEN)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .build());
            firework.setFireworkMeta(fireworkMeta);
            Bukkit.broadcastMessage(Prefixes.BEDCRAFT+" §aBienvenue sur le serveur §6"+player.getDisplayName()+" §a!!");
        }
    }
}
