package fr.leroideskiwis.bedcraft.duel;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Duel {

    private Player[] players = new Player[2];
    private Location location;

    public Duel(Player[] players, Location location){
        this.location = location;
        this.players = players;
    }

    public void start(){

    }

    public boolean isLocation(Location location){
        return this.location.equals(location);
    }

}
