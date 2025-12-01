package sbs.immovablerod.metaDungeon.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

import java.util.List;

public class Tasks {
    public static void removeChests(List<Location> locations) {
        System.out.println("removing old chests at " + locations.size() + " locations");
        for  (Location location : locations) {
            try {
                Chest chest = (Chest) location.getBlock().getState();
                chest.getInventory().clear();
                location.getBlock().setType(Material.AIR);
            } catch (ClassCastException ignored) {System.out.println("[WARN] could not clear chest!");}
        }
    }
}
