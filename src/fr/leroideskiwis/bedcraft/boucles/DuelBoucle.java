package fr.leroideskiwis.bedcraft.boucles;

import fr.leroideskiwis.bedcraft.duel.Duel;
import fr.leroideskiwis.bedcraft.player.DuelPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelBoucle implements Runnable{

    private Duel duel;

    public DuelBoucle(Duel duel) {
        this.duel = duel;
    }

    @Override
    public void run() {

        duel.forEach(duelPlayer -> {
            Player player = duelPlayer.getPlayer();
            PlayerInventory inventory = player.getInventory();
            ItemStack itemstack = inventory.getItem(0);
            if(itemstack == null || itemstack.getType() == Material.AIR || itemstack.getAmount() <= 5) inventory.setItem(0, new ItemStack(Material.DIRT, 10));
            player.setFoodLevel(19);
            duelPlayer.setBed();
            DuelPlayer otherPlayer = duel.getOtherPlayer(duelPlayer);
            if(!duelPlayer.hasBed()){
                otherPlayer.sendMessage("§aLe bloc de l'équipe adverse a été détruit !");
                duelPlayer.sendMessage("§cVotre bloc a été détruit !");
                duel.forEach(duelPlayer1 -> duelPlayer.getPlayer().playSound(duelPlayer1.getPlayer().getLocation(), Sound.ENTITY_WITHER_SPAWN, 100, 1));
            }
        });

    }
}
