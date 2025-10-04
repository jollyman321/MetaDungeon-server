package sbs.immovablerod.metaDungeon.util;

import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;

public class AddPlayer {
    private final static MetaDungeon plugin = MetaDungeon.getInstance();

    public static MetaDungeonPlayer register (Player player) {
        MetaDungeonPlayer out = new MetaDungeonPlayer(player);
        return out;

    }
}
