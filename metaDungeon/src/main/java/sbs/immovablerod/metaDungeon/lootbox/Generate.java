package sbs.immovablerod.metaDungeon.lootbox;

import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.Bukkit.getWorlds;

public class Generate {

    private static final World world = getWorlds().get(0);

    private static boolean check_block(int x, int y, int z) {

        if (world.getBlockAt(x, y, z).getType() == Material.AIR &&
                world.getBlockAt(x, y + 1, z).getType() == Material.AIR &&
                world.getBlockAt(x, y - 1, z).getType() != Material.AIR) return true;
        return false;
    }
    public static List<List<Integer>> create_lot_box_map(int x, int y, int z, int xdelta, int ydelta, int zdelta) {
        List<List<Integer>> valid_points = new ArrayList<>();
        for (int i = x; i < x + xdelta; i++) {
            for (int j = y; j < y + ydelta; j++) {
                for (int k = z; k < z + zdelta; k++) {
                    if (check_block(i, j, k)) {
                        List<Integer> point = new ArrayList<>();
                        point.add(i);
                        point.add(j);
                        point.add(k);
                        valid_points.add(point);
                    }
                }
            }
        }
        return valid_points;
    }

}