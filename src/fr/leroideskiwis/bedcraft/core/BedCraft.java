package fr.leroideskiwis.bedcraft.core;

import fr.leroideskiwis.bedcraft.boucles.CreationBoucle;
import fr.leroideskiwis.bedcraft.builders.ConnectionDataBuilder;
import fr.leroideskiwis.bedcraft.commands.*;
import fr.leroideskiwis.bedcraft.listeners.CreationEvents;
import fr.leroideskiwis.bedcraft.listeners.JoinLeaveEvents;
import fr.leroideskiwis.bedcraft.listeners.MenuEvents;
import fr.leroideskiwis.bedcraft.listeners.UtilsEvents;
import fr.leroideskiwis.bedcraft.managers.CustomPlayerManager;
import fr.leroideskiwis.bedcraft.menus.MenuManager;
import fr.leroideskiwis.bedcraft.shop.Shop;
import fr.leroideskiwis.bedcraft.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class BedCraft extends JavaPlugin {

    private final SQLManager sqlManager = new SQLManager();
    private CustomPlayerManager customPlayerManager;
    private Shop shop;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PluginManager pluginManager = getServer().getPluginManager();
        this.customPlayerManager = new CustomPlayerManager(this, sqlManager);
        Bukkit.getScheduler().runTaskTimer(this, new CreationBoucle(customPlayerManager), 20, 20);
        this.shop = new Shop(sqlManager);
        this.menuManager = new MenuManager();
        pluginManager.registerEvents(new JoinLeaveEvents(customPlayerManager, sqlManager, shop), this);
        pluginManager.registerEvents(new CreationEvents(customPlayerManager), this);
        pluginManager.registerEvents(new UtilsEvents(), this);
        pluginManager.registerEvents(new MenuEvents(customPlayerManager, menuManager, shop), this);
        getCommand("creation").setExecutor(new CreationCommand(customPlayerManager, shop));
        getCommand("duel").setExecutor(new DuelCommand(customPlayerManager));
        getCommand("shop").setExecutor(new ShopCommand(customPlayerManager, shop, menuManager));
        getCommand("registerblock").setExecutor(new RegisterCommand(shop));
        getCommand("stats").setExecutor(new StatsCommand(customPlayerManager));

        try {
            Bukkit.broadcastMessage("Connexion à la base de donnée...");
            sqlManager.connect(new ConnectionDataBuilder()
                    .withDatabase(getConfig().getString("database.name"))
                    .withUser(getConfig().getString("database.user"))
                    .withPassword(getConfig().getString("database.password"))
                    .withport(getConfig().getInt("database.port"))
                    .withHost(getConfig().getString("database.host"))
                    .build());

            Bukkit.broadcastMessage("Chargement des joueurs");
            for(Player player : Bukkit.getOnlinePlayers()){
                customPlayerManager.registerAndLoad(player, shop);
            }

            Bukkit.broadcastMessage("Chargement de la boutique");

            shop.load();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("La connexion à la base de donnée a échouée ! Arrêt du plugin...");
            pluginManager.disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        try {
            customPlayerManager.saveAll();
            shop.save();
            sqlManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Une erreur s'est produite. La sauvegarde n'a pas pu être effectuée.");
        }
    }
}
