package sbs.immovablerod.metaDungeon.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.enums.Colors;

import java.util.*;

import static sbs.immovablerod.metaDungeon.util.Random.getRandInt;

public class MessageManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final List<Player> players;

    public MessageManager() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }
    public void clear() {
        this.players.clear();
    }

    public void messageAll(String message, Colors color) {
        for (Player player : this.players) {
            player.sendMessage(color.code() + message);
        }
    }

    public void titleAll(String message, String subTitle, Colors color, Colors subcolor) {
        for (Player player : this.players) {
            player.sendTitle(color.code() + message, subcolor.code() + subTitle, 20, 80, 20);
        }
    }

    public void messageAllRepeat(String message, Colors color, Integer repeatCount) {
        // @time = repeatCount
        for (Player player : this.players) {
            player.sendMessage(message.replace("@time", repeatCount.toString()));
        }
        if (repeatCount > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                messageAllRepeat(message, color, repeatCount - 1);
            }, 20L);
        }
    }

}
