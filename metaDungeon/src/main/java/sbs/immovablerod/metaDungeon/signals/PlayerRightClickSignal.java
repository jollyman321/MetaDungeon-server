package sbs.immovablerod.metaDungeon.signals;

import org.bukkit.entity.Player;

public class PlayerRightClickSignal extends BaseSignal {

    private final Player player;

    public PlayerRightClickSignal(Player player) {
        super();
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
