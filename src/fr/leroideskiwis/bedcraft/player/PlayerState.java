package fr.leroideskiwis.bedcraft.player;

import fr.leroideskiwis.bedcraft.modes.CreationMode;
import fr.leroideskiwis.bedcraft.modes.DuelMode;
import fr.leroideskiwis.bedcraft.modes.IdleMode;
import fr.leroideskiwis.bedcraft.modes.PlayerMode;

public enum PlayerState {
    IDLE(new IdleMode()), DUEL(new DuelMode()), CREATION(new CreationMode());

    public final PlayerMode playerMode;

    PlayerState(PlayerMode playerMode) {
        this.playerMode = playerMode;
    }
}
