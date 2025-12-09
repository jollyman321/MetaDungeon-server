package sbs.immovablerod.metaDungeon.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import sbs.immovablerod.metaDungeon.MetaDungeon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class BuildMap {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final World world;
    public BuildMap (World world, String name, Location pos1, Location pos2, Location playerSpawnPos) throws IOException {
        plugin.getLogger().info("Creating map...");
        this.world = world;
        // always start at the most negative pos
        int xStart = min(pos1.getBlockX(), pos2.getBlockX());
        int yStart = min(pos1.getBlockY(), pos2.getBlockY());
        int zStart = min(pos1.getBlockZ(), pos2.getBlockZ());

        int xDelta = max(pos1.getBlockX(), pos2.getBlockX()) - xStart;
        int yDelta = max(pos1.getBlockY(), pos2.getBlockY()) - yStart;
        int zDelta = max(pos1.getBlockZ(), pos2.getBlockZ()) - zStart;

        plugin.getLogger().info("pos " + xStart + " "+ yStart + " "+ zStart + " ");
        plugin.getLogger().info("delta " + xDelta + " "+ yDelta + " "+ zDelta + " ");

        Integer[] playerSpawn = new Integer[]{playerSpawnPos.getBlockX(), playerSpawnPos.getBlockY(), playerSpawnPos.getBlockZ()};

        List<Integer[]> chestLocations = new ArrayList<>();
        List<Integer[]> entityLocations = new ArrayList<>();
        List<Integer[]> oversizedEntityLocations = new ArrayList<>();
        for (int x = xStart; x < xStart + xDelta; x++) {
            for (int y = yStart; y < yStart + yDelta; y++) {
                for (int z = zStart; z < zStart + zDelta; z++) {
                    if (isValidChestSpawn(x, y, z)) {
                        chestLocations.add(new Integer[]{x, y, z});
                    }
                    if (isValidEntitySpawn(x, y, z)) {
                        entityLocations.add(new Integer[]{x, y, z});
                    }
                    if (isValidOversizedEntitySpawn(x, y, z)) {
                        oversizedEntityLocations.add(new Integer[]{x, y, z});
                    }
                }
            }
        }
        plugin.getLogger().info("found " + chestLocations.size() + " valid chest points");
        plugin.getLogger().info("found " + entityLocations.size() + " valid entity points");
        plugin.getLogger().info("found " + oversizedEntityLocations.size() + " valid oversized entity points");


        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        File file = new File(plugin.jsonLoader.mapDataPath + File.separator + name + ".json");

        plugin.getLogger().info("map saved to " + plugin.jsonLoader.mapDataPath + File.separator + name + ".json");


        writer.writeValue(file, new SerializableMap(name, playerSpawn, chestLocations, entityLocations, oversizedEntityLocations));

    }
    private boolean isValidChestSpawn(int x, int y, int z) {
        return this.world.getBlockAt(x, y, z).getType() == Material.AIR &&
                this.world.getBlockAt(x, y + 1, z).getType() == Material.AIR &&
                this.world.getBlockAt(x, y - 1, z).getType() != Material.AIR;
    }
    private boolean isValidEntitySpawn(int x, int y, int z) {
        return this.world.getBlockAt(x, y, z).isPassable() &&
                this.world.getBlockAt(x, y + 1, z).isPassable() &&
                this.world.getBlockAt(x, y - 1, z).getType() != Material.AIR;
    }
    private boolean isValidOversizedEntitySpawn(int x, int y, int z) {
        if (!this.world.getBlockAt(x, y - 1, z).isSolid()) {
            return false;
        }
        for (int i = x - 1; i < x + 1; i++) {
            for (int j = y; j < y + 2; j++) {
                for (int k = z - 1; k < z + 1; k++) {
                    if(!this.world.getBlockAt( i, j, k).isPassable()) {
                        return false;
                    }
                }
            }
        }
        return  true;
    }



}
