package fr.leroideskiwis.bedcraft.duel;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.DuelPlayer;
import fr.leroideskiwis.bedcraft.player.PlayerState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.function.Consumer;

public class Duel {

    private DuelPlayer player1;
    private DuelPlayer player2;
    private Location location;

    public Duel(DuelPlayer player1, DuelPlayer player2, Location location){
        this.location = location;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void start(){
        forEach(customPlayer -> customPlayer.getCustomPlayer().setPlayerState(PlayerState.DUEL));
        forEach(DuelPlayer::setBedLocation);
        forEach(DuelPlayer::copyBase);
        teleportPlayers();
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

    public boolean hasPlayer(Player player) {
        return player1.getPlayer().equals(player) || player2.getPlayer().equals(player1);
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
}
