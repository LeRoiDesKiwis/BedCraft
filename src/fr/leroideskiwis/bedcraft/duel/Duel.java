package fr.leroideskiwis.bedcraft.duel;

import fr.leroideskiwis.bedcraft.boucles.DuelBoucle;
import fr.leroideskiwis.bedcraft.core.BedCraft;
import fr.leroideskiwis.bedcraft.core.Prefixes;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.DuelPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.function.Consumer;

public class Duel {

    private final BedCraft bedCraft;
    private DuelPlayer player1;
    private DuelPlayer player2;
    private Location location;

    public Duel(BedCraft bedCraft, DuelPlayer player1, DuelPlayer player2, Location location){
        this.location = location;
        this.player1 = player1;
        this.player2 = player2;
        this.bedCraft = bedCraft;
    }

    public void start(){
        forEach(duelPlayer -> duelPlayer.getCustomPlayer().setPlayerState(PlayerState.DUEL));
        forEach(DuelPlayer::setBedLocation);
        forEach(DuelPlayer::copyBase);
        forEach(duelPlayer -> {
            Player player = duelPlayer.getPlayer();
            player.getInventory().clear();
            player.setAllowFlight(false);
            player.setHealth(20f);
            player.setFoodLevel(20);
        });
        teleportPlayers();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(bedCraft, new DuelBoucle(this), 0, 1);
    }

    public void teleportPlayers(){
        player1.teleport();
        player2.teleport();
    }

    public boolean isLocation(Location location){
        return this.location.equals(location);
    }

    public void forEach(Consumer<DuelPlayer> consumer){
        consumer.accept(player1);
        consumer.accept(player2);
    }

    public DuelPlayer getOtherPlayer(DuelPlayer duelPlayer){
        return duelPlayer.equals(player1) ? player2 : player1;
    }

    public boolean hasPlayer(Player player) {
        return player1.getPlayer().equals(player) || player2.getPlayer().equals(player);
    }

    public Optional<DuelPlayer> getDuelPlayer(CustomPlayer customPlayer){
        if(player1.getCustomPlayer().equals(customPlayer)){
            return Optional.of(player1);
        } else if(player2.getCustomPlayer().equals(customPlayer)){
            return Optional.of(player2);
        }
        return Optional.empty();
    }

    public void clearDuel(){
        forEach(DuelPlayer::clearBlocks);
    }

    public void kill(DuelPlayer duelPlayer) {
        DuelPlayer otherPlayer = duelPlayer.equals(player1) ? player2 : player1;
        otherPlayer.giveReward();
        duelPlayer.sendMessage("§cVous avez perdu ce duel...");
        otherPlayer.sendMessage("§aBravo ! Vous avez gagné ce duel !");
        Bukkit.broadcastMessage(Prefixes.BEDCRAFT+" §3bravo à §e"+otherPlayer.getPlayer().getDisplayName()+"§3 qui a gagné la partie contre §e"+duelPlayer.getPlayer().getDisplayName());
        forEach(duelPlayer1 -> duelPlayer1.getCustomPlayer().setPlayerState(PlayerState.IDLE));
        clearDuel();
    }

    public void respawn(DuelPlayer duelPlayer) {
        forEach(duelPlayer1 -> duelPlayer.sendMessage("§c"+duelPlayer1.getPlayer().getDisplayName()+" est mort !"));
    }
}
