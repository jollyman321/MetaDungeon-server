package sbs.immovablerod.metaDungeon.game;

import org.bukkit.Bukkit;
import sbs.immovablerod.metaDungeon.Enums.Colors;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;


import java.util.Collection;

public class Message {
    private static final MetaDungeon plugin = MetaDungeon.getInstance();
    public static void messageAll(String message, Colors color) {
        for (MetaDungeonPlayer player : plugin.players.values()) {
            player.getPlayer().sendMessage(color.code() + message);
        }
    }
    public static void titleAll(String message, String subTitle, Colors color, Colors subcolor) {
        for (MetaDungeonPlayer player : plugin.players.values()) {
            player.getPlayer().getPlayer().sendTitle(color.code() + message, subcolor.code() + subTitle, 20, 80, 20);
        }
    }
    public static void messageAllRepeat(String message, Colors color, Integer repeatCount) {
        // @time = repeatCount
        for (MetaDungeonPlayer player : plugin.players.values()) {
            player.getPlayer().sendMessage(message.replace("@time", repeatCount.toString()));
        }
        if (repeatCount > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {messageAllRepeat(message, color, repeatCount - 1);}, 20L);
        }
    }
    public static void showPlayerStats(Collection<MetaDungeonPlayer> players) {
        // @time
        for (MetaDungeonPlayer player : players) {
            player.displayActionBar();
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {showPlayerStats(players);}, 20L);

    }
}
