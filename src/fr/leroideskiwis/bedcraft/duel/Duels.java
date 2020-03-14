package fr.leroideskiwis.bedcraft.duel;

import fr.leroideskiwis.bedcraft.builders.TextComponentBuilder;
import fr.leroideskiwis.bedcraft.core.BedCraft;
import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.player.DuelPlayer;
import fr.leroideskiwis.bedcraft.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Duels {

    private static final String DUEL_WORLD = "duel";
    private Map<Player, Player> requests = new HashMap<>();
    private List<Duel> duels = new ArrayList<>();
    private CustomPlayerManager customPlayerManager;
    private BedCraft bedCraft;

    public Duels(BedCraft bedcraft, CustomPlayerManager customPlayerManager){
        this.customPlayerManager = customPlayerManager;
        this.bedCraft = bedcraft;
    }

    public void request(Player player, Player target){
        requests.put(target, player);
        player.sendMessage("§aLa demande a été envoyée !");
        target.sendMessage("");
        target.sendMessage("§e---- §aDEMANDE DE DUEL §e----");
        target.sendMessage("§7Le joueur §e"+player.getDisplayName()+" §7vous a demandé en duel.");
        target.sendMessage("");
        target.spigot().sendMessage(new TextComponentBuilder("§aPour accepter, tapez la commande /daccept ou cliquez ici.")
                .setClickEvent(ClickEvent.Action.RUN_COMMAND, "/daccept")
                .setHoverEvent(HoverEvent.Action.SHOW_TEXT, "§aAccepter le duel")
                .build());
        target.spigot().sendMessage(new TextComponentBuilder("§cPour refuser, tapez le commande /ddeny ou cliquez ici.")
                .setClickEvent(ClickEvent.Action.RUN_COMMAND, "/ddeny")
                .setHoverEvent(HoverEvent.Action.SHOW_TEXT, "§cRefuser le duel")
                .build());
        target.sendMessage("§e-----------------------");
        target.sendMessage("");
    }

    public boolean hasRequest(Player player){
        return requests.containsKey(player) || requests.containsValue(player);
    }

    public boolean isInDuel(Player player){
        return duels.stream().anyMatch(duel -> duel.hasPlayer(player));
    }

    public void accept(Player player){
        Player target = requests.get(player);
        requests.remove(player);
        target.sendMessage("§aLe joueur "+player.getDisplayName()+" a accepté votre requête, la partie va commencer...");
        player.sendMessage("§aVous avez accepté la requête de "+target.getDisplayName()+", la partie va commencer...");
        start(player, target);
    }

    public void deny(Player player){
        Player target = requests.get(player);
        target.sendMessage("§cLe joueur "+player.getDisplayName()+" a refusé votre requête.");
        player.sendMessage("§cVous avez refusé la requête de "+target.getDisplayName()+".");
        requests.remove(player);
    }

    private void start(Player player, Player target) {
        Location location = foundLocation();
        CustomPlayer customPlayer = customPlayerManager.getCustomPlayer(player).get();
        CustomPlayer targetPlayer = customPlayerManager.getCustomPlayer(target).get();

        DuelPlayer player1 = new DuelPlayer(location, customPlayer);
        DuelPlayer player2 = new DuelPlayer(Utils.add(location, 100, 0, 20), targetPlayer);
        Duel duel = new Duel(bedCraft, player1, player2, location);
        duels.add(duel);
        duel.start();
    }

    public Optional<DuelPlayer> getDuelPlayer(CustomPlayer customPlayer){
        return duels.stream().map(duel -> duel.getDuelPlayer(customPlayer)).filter(Optional::isPresent).map(Optional::get).findAny();
    }

    public Optional<Duel> getDuel(CustomPlayer customPlayer){
        return duels.stream().filter(duel -> duel.getDuelPlayer(customPlayer).isPresent()).findAny();
    }

    public Location foundLocation(){
        for(int x = -100000; x < 100000; x+=1000){
            for(int z = -100000; z < 100000; z+=1000){

                Location location = new Location(Bukkit.getWorld(DUEL_WORLD), x, 60, z);
                if(duels.stream().noneMatch(duel -> duel.isLocation(location))) return location;

            }
        }
        return new Location(Bukkit.getWorld(DUEL_WORLD), 0, 70, 0);
    }

    public void clearAll(){
        duels.forEach(Duel::clearDuel);
    }

    public void kill(DuelPlayer duelPlayer) {
        Optional<Duel> duelOpt = getDuel(duelPlayer.getCustomPlayer());
        duelOpt.ifPresent(duel -> {
            duel.kill(duelPlayer);
            duels.remove(duel);
        });

    }

    public void respawn(DuelPlayer duelPlayer) {
        Optional<Duel> duelOpt = getDuel(duelPlayer.getCustomPlayer());
        duelOpt.ifPresent(duel -> {
            duel.respawn(duelPlayer);
            duelPlayer.teleport();
        });
    }
}
