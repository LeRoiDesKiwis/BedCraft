package fr.leroideskiwis.bedcraft.modes;

import fr.leroideskiwis.bedcraft.player.CustomPlayer;

import java.sql.SQLException;

public interface PlayerMode {

    void init(CustomPlayer customPlayer) throws SQLException;
}
