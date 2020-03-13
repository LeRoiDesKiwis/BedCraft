package fr.leroideskiwis.bedcraft.managers;

import fr.leroideskiwis.bedcraft.core.BedCraft;
import fr.leroideskiwis.bedcraft.duel.Duels;
import fr.leroideskiwis.bedcraft.player.CustomPlayer;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.shop.ShopInventory;
import fr.leroideskiwis.bedcraft.sql.SQLManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class CustomPlayerManager {

    private final List<CustomPlayer> customPlayers = new ArrayList<>();

    private final SQLManager sqlManager;
    private final BedCraft bedcraft;
    private Duels duels;

    public CustomPlayerManager(Duels duels, BedCraft bedCraft, SQLManager sqlManager){

        this.bedcraft = bedCraft;
        this.sqlManager = sqlManager;
    }

    public CustomPlayer registerAndLoad(Player player, Shop shop) throws SQLException {
        CustomPlayer customPlayer = new CustomPlayer(duels, player, shop, sqlManager, bedcraft);
        customPlayers.add(customPlayer);
        ShopInventory shopInventory = customPlayer.shopInventory;

        customPlayer.load();
        return customPlayer;
    }

    public void saveAll() throws SQLException {
        for (CustomPlayer customPlayer : customPlayers) {
            customPlayer.save();
        }
    }

    public Optional<CustomPlayer> getById(int id){
        return customPlayers.stream().filter(customPlayer -> customPlayer.getId() == id).findAny();
    }

    public Optional<CustomPlayer> getCustomPlayer(UUID uuid){
        return customPlayers.stream().filter(customPlayer -> customPlayer.getUUID().equals(uuid)).findAny();
    }

    public Optional<CustomPlayer> getCustomPlayer(Player player){
        return getCustomPlayer(player.getUniqueId());
    }

    public List<CustomPlayer> getCustomPlayers(){
        return new ArrayList<>(customPlayers);
    }

    public void remove(Player player) {
        getCustomPlayer(player.getUniqueId()).ifPresent(customPlayer -> {
            try {
                customPlayer.save();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            customPlayers.remove(customPlayer);
        });
    }
}
