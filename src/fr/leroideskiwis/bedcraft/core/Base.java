package fr.leroideskiwis.bedcraft.core;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.shop.ShopItem;
import fr.leroideskiwis.bedcraft.sql.SQLManager;
import fr.leroideskiwis.bedcraft.utils.Corners;
import fr.leroideskiwis.bedcraft.utils.Interval;
import fr.leroideskiwis.bedcraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Base {

    private final SQLManager sqlManager;
    private final CustomPlayer customPlayer;

    public Base(CustomPlayer customPlayer, SQLManager sqlManager){
        this.customPlayer = customPlayer;
        this.sqlManager = sqlManager;
    }

    public void teleport() throws SQLException {
        if(getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR) getLocation().add(0, -1, 0).getBlock().setType(Material.STONE);
        customPlayer.player.teleport(getLocation());
    }

    public Location getLocation() throws SQLException {
        return new Location(Bukkit.getWorld(getString("world")), getInt("x"), getInt("y"), getInt("z"));
    }

    public boolean isIn() throws SQLException {
        return isIn(customPlayer.player.getLocation());
    }

    public boolean isIn(Location location) throws SQLException {
        return Interval.isInBorder(getCorners().cornerXmin-10, getCorners().cornerXmax+10, getCorners().cornerZmin-10, getCorners().cornerZmax+10, location);
    }

    public Corners getCorners() throws SQLException {
        return new Corners(getInt("x")-getInt("widthX")/2, getInt("x")+getInt("widthX")/2, getInt("z")-getInt("widthZ")/2,getInt("z")+getInt("widthX")/2);
    }
    /**
     *
     * @param label The label of the column (can be widthX, widthY, height, width, x, y or x)
     * @return the object in the sql database
     */

    public Object getObject(String label) throws SQLException {
        PreparedStatement preparedStatement = sqlManager.prepareStatement("SELECT * FROM bases WHERE id=?");
        preparedStatement.setInt(1, customPlayer.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) return null;
        Object toReturn = resultSet.getObject(label);
        resultSet.close();
        return toReturn;
    }

    public int getInt(String label) throws SQLException {
        return (int)getObject(label);
    }

    public String getString(String label) throws SQLException {
        return (String)getObject(label);
    }

    public void copyTo(Location location) throws SQLException {

        for(int x = 0; x < getInt("widthX"); x++){
            for (int y = 0; y < getInt("height"); y++) {
                for (int z = 0; z < getInt("widthZ"); z++) {
                    Block blockFrom = location.getBlock();
                    Block blockTo = getLocation().getBlock();

                    blockFrom.setType(blockTo.getType());
                    blockFrom.setData(blockTo.getData());

                }
            }
        }

    }

    public void init() throws SQLException {

        if(getObject("x") == null){

            PreparedStatement preparedStatement = sqlManager.prepareStatement("INSERT INTO bases " +
                    "(id, widthX, widthZ, height, x, y, z, world) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");

            Location location = getNewLocation();

            preparedStatement.setInt(1, customPlayer.getId());
            preparedStatement.setInt(2, 20);
            preparedStatement.setInt(3, 20);
            preparedStatement.setInt(4, 100);
            preparedStatement.setInt(5, location.getBlockX());
            preparedStatement.setInt(6, location.getBlockY());
            preparedStatement.setInt(7, location.getBlockZ());
            preparedStatement.setString(8, location.getWorld().getName());

            customPlayer.player.sendMessage("§aVotre base du mode création à été définie en x: "+location.getBlockX()+", y: "+location.getBlockY()+", z: "+location.getBlockZ()+".");

            preparedStatement.execute();
            preparedStatement.close();
        }
    }

    public Location getNewLocation() throws SQLException {
        PreparedStatement preparedStatement = sqlManager.prepareStatement("SELECT * FROM bases WHERE x=? AND z=?");
        for(int x = 0; x < 10000000; x+=176){
            for(int z = 0; z < 10000000; z+=176){
                preparedStatement.setInt(1, x);
                preparedStatement.setInt(2, z);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(!resultSet.next()) {
                    resultSet.close();
                    return new Location(Bukkit.getWorld("creation"), x, 60, z);
                }
                x += resultSet.getInt("widthX");
                z += resultSet.getInt("widthZ");
                resultSet.close();

            }
        }
        preparedStatement.close();
        customPlayer.player.sendMessage("§cNous n'avons pas pu trouver d'emplacement libre... Veuillez contacter un administrateur.");
        return new Location(Bukkit.getWorld("world"), 0, 0, 0);
    }

}
