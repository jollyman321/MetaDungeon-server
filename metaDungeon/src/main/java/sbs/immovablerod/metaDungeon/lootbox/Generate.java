package sbs.immovablerod.metaDungeon.lootbox;

import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.Bukkit.getWorlds;
import static sbs.immovablerod.metaDungeon.util.IO.LoadMapFromJson;

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

    public static void place_chests(int density) {
        // density chest rate per blocks 1/density

        Map<String,Object> mapdata = LoadMapFromJson("mapdata.json");

        List<List<Integer>> valid_loot_box_points = (List<List<Integer>>) mapdata.get("valid_loot_box_points");
        // (List<List<Integer>>) convertObjectToList(

        for (List<Integer> validLootBoxPoint : valid_loot_box_points) {
            if (ThreadLocalRandom.current().nextInt(1, (int) (density + 1)) == 1) {
                world.getBlockAt(validLootBoxPoint.get(0),
                        validLootBoxPoint.get(1),
                        validLootBoxPoint.get(2)).setType(Material.CHEST);
            }
        }
    }
}