package fr.leroideskiwis.bedcraft.duel;

import fr.leroideskiwis.bedcraft.builders.TextComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Duels {

    private static final String DUEL_WORLD = "duel";
    private Map<Player, Player> requests = new HashMap<>();
    private List<Duel> duels = new ArrayList<>();

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
        Player[] players = {player, target};
        Location location = foundLocation();
        Duel duel = new Duel(players, location);
        duels.add(duel);
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

}
