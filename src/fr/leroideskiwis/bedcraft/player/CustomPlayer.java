package fr.leroideskiwis.bedcraft.player;

import fr.leroideskiwis.bedcraft.core.Base;
import fr.leroideskiwis.bedcraft.core.BedCraft;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.shop.ShopInventory;
import fr.leroideskiwis.bedcraft.sql.SQLManager;
import fr.leroideskiwis.bedcraft.utils.Interval;
import fr.leroideskiwis.bedcraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CustomPlayer {

    private final SQLManager sqlManager;
    private int exp;
    private int gold;
    public final Base base;
    public final Player player;
    private PlayerState playerState = PlayerState.IDLE;
    public final ShopInventory shopInventory;
    private BedCraft bedCraft;

    private int id;

    public int getGold(){
        return gold;
    }

    public CustomPlayer(Player player, Shop shop, SQLManager sqlManager, BedCraft bedCraft) {
        this.player = player;
        this.shopInventory = new ShopInventory(this, shop, sqlManager);
        this.base = new Base(this, sqlManager);
        this.sqlManager = sqlManager;
        this.bedCraft = bedCraft;
    }

    public boolean isIn(int borderX, int borderX2, int borderZ, int borderZ2){
        return Interval.isInBorder(borderX, borderX2, borderZ, borderZ2, player.getLocation());
    }

    public void load() throws SQLException {
        PreparedStatement preparedStatement = sqlManager.prepareStatement("SELECT * FROM players WHERE uuid=?");
        preparedStatement.setString(1, player.getUniqueId().toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.first()){
            save();
            load();
            return;
        }

        this.exp = resultSet.getInt("exp");
        this.gold = resultSet.getInt("gold");
        this.id = resultSet.getInt("id");

        resultSet.close();
        shopInventory.load();
        preparedStatement.close();
        base.init();

    }

    public boolean isState(PlayerState playerState){
        return this.playerState == playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        player.sendMessage("§cVous quittez le mode "+this.playerState.toString().toLowerCase());
        this.playerState = playerState;
        player.sendMessage("§aVous êtes désormais en mode "+this.playerState.toString().toLowerCase());
        try {
            playerState.playerMode.init(this);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public void toggle(PlayerState from, PlayerState to) {
        if(isState(from)) setPlayerState(to);
        else if(isState(to)) setPlayerState(from);
    }

    public int getLevel(){
        return exp == 0 ? 0 : (int)Math.log(exp);
    }

    public UUID getUUID(){
        return player.getUniqueId();
    }

    public void save() throws SQLException {
        PreparedStatement preparedStatement = sqlManager.prepareStatement("INSERT INTO players (uuid, exp, gold) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE exp=VALUES(exp), gold=VALUES(gold)");
        preparedStatement.setString(1, getUUID().toString());
        preparedStatement.setInt(2, exp);
        preparedStatement.setInt(3, gold);

        preparedStatement.execute();
        preparedStatement.close();
        shopInventory.save();
        base.save();
    }

    public boolean hasEnough(int gold){
        return this.gold >= gold;
    }

    public void removeGold(int gold){
        this.gold -= gold;
    }

    public int getId(){
        return id;
    }

    public void sendMessage(String string) {
        player.sendMessage(string);
    }

    public void teleportToSpawn(){
        ConfigurationSection config = bedCraft.getConfig().getConfigurationSection("locations.hub");
        player.teleport(new Location(Bukkit.getWorld(config.getString("world")), config.getInt("x"), config.getInt("y"), config.getInt("z")));
    }

    public int getExp() {
        return exp;
    }

    public boolean isIn(Block block) throws SQLException {
        return base.isIn(block.getLocation());
    }

    public void placeBlock(Location location) throws SQLException {
        base.addBlock(location);
    }

    public void destroyBlock(Block block) throws SQLException {
        block.setType(Material.AIR);
        base.removeBlock(block.getLocation());
        shopInventory.setInventoryPlayer();
    }

    public void addGold(int gold) {
        this.gold+=gold;
    }

    public String getName() {
        return player.getDisplayName();
    }
}
