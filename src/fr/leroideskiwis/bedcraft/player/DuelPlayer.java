package fr.leroideskiwis.bedcraft.player;

import fr.leroideskiwis.bedcraft.boucles.DuelBoucle;
import fr.leroideskiwis.bedcraft.core.BedCraft;
import fr.leroideskiwis.bedcraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DuelPlayer {

    private final Location baseLocation;
    private final CustomPlayer customPlayer;
    private final Player player;
    private List<Location> locations = new ArrayList<>();
    private Location bedLocation;
    private static final Material BED_TYPE = Material.PUMPKIN;

    public DuelPlayer(Location baseLocation, CustomPlayer customPlayer) {
        this.baseLocation = baseLocation;
        this.customPlayer = customPlayer;
        this.player = customPlayer.player;
    }

    public void copyBase(){
        locations.addAll(customPlayer.base.copyTo(baseLocation));
        this.bedLocation = locations.stream().filter(location -> location.getBlock().getType() == BED_TYPE).findAny().orElse(null);
    }

    public Location getBaseLocation() {
        return baseLocation;
    }

    public CustomPlayer getCustomPlayer() {
        return customPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public void teleport(){
        player.teleport(Utils.add(baseLocation, 0, 1, 0));
    }

    public void setBedLocation(){
        player.setBedSpawnLocation(baseLocation, true);
    }

    public void addLocation(Location location){
        locations.add(location);
    }

    public void removeLocation(Location location){
        locations.remove(location);
    }

    public void clearBlocks() {
        for(Location location : locations){
            location.getBlock().setType(Material.AIR);
        }
    }

    public void setBed(){
        if(!hasBed()) this.bedLocation = null;
    }

    public boolean hasBed(){
        return bedLocation != null && bedLocation.getBlock().getType() == BED_TYPE;
    }

    public void sendMessage(String message){
        player.sendMessage(message);
    }

    public void giveReward(){
        customPlayer.addGold(100);
    }
}
