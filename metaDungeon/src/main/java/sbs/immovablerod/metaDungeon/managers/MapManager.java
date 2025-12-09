package sbs.immovablerod.metaDungeon.managers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.util.Random;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private static final Logger log = LogManager.getLogger(MapManager.class);
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String mapName;
    private JsonNode currentMap;
    private List<int[]> chestLocations;
    private List<int[]> entityLocations;
    private List<int[]> oversizedEntityLocations;
    private List<int[]> playerSpawnPoints;
    private boolean initialized = false;

    public MapManager() {
        this.currentMap = null;
        this.mapName = null;
    }

    public void setCurrentMap(String map) {
        this.currentMap = plugin.jsonLoader.mapTemplates.path(map);
        this.mapName = map;

        this.chestLocations = new ArrayList<>();
        this.entityLocations = new ArrayList<>();
        this.oversizedEntityLocations = new ArrayList<>();
        this.playerSpawnPoints = new ArrayList<>();

        try {
            JsonNode mapData = this.objectMapper.readTree(new File(plugin.jsonLoader.mapDataPath + File.separator + this.currentMap.path("internalName").asText() + ".json"));

            final JsonNode chestLocationsRaw = mapData.get("chestLocations");
            if (chestLocationsRaw.isArray()) {
                chestLocationsRaw.forEach(loc -> this.chestLocations.add(new int[]{loc.get(0).asInt(), loc.get(1).asInt(), loc.get(2).asInt()}));
            }

            final JsonNode entityLocationsRaw = mapData.get("entityLocations");
            if (entityLocationsRaw.isArray()) {
                entityLocationsRaw.forEach(loc -> this.entityLocations.add(new int[]{loc.get(0).asInt(), loc.get(1).asInt(), loc.get(2).asInt()}));
            }

            final JsonNode oversizedEntityLocationsRaw = mapData.get("oversizedEntityLocations");
            if (oversizedEntityLocationsRaw.isArray()) {
                oversizedEntityLocationsRaw.forEach(loc -> this.oversizedEntityLocations.add(new int[]{loc.get(0).asInt(), loc.get(1).asInt(), loc.get(2).asInt()}));
            }

            final JsonNode playerSpawnPointsRaw = plugin.jsonLoader.mapTemplates.path(this.mapName).path("playerSpawnPoints");
            if (playerSpawnPointsRaw.isArray()) {
                playerSpawnPointsRaw.forEach(loc -> this.playerSpawnPoints.add(new int[]{loc.get(0).asInt(), loc.get(1).asInt(), loc.get(2).asInt()}));
            }


            this.initialized = true;
            plugin.getLogger().info("loaded map: " + this.mapName);
            plugin.getLogger().info("chest locations=" + this.chestLocations.size());
            plugin.getLogger().info("entity locations=" + this.entityLocations.size());
            plugin.getLogger().info("oversized entity locations=" + this.oversizedEntityLocations.size());
            plugin.getLogger().info("player spawn points=" + this.playerSpawnPoints.toString());

        } catch (IOException e) {
            log.error("e: ", e);
            this.initialized = false;
        }
    }

    public Location getChestLocation() {
        // cache Locations?
        int[] point = this.chestLocations.get(Random.getRandInt(1, this.chestLocations.size() - 1));
        return new Location(plugin.world, point[0], point[1], point[2]);
    }

    public Location getEntityLocation() {
        // cache Locations?
        int[] point = this.entityLocations.get(Random.getRandInt(1, this.entityLocations.size() - 1));
        return new Location(plugin.world, point[0], point[1], point[2]);
    }

    public Location getPlayerSpawnPoint() {
        // cache Locations?
        int[] point = this.playerSpawnPoints.get(Random.getRandInt(1, this.playerSpawnPoints.size() - 1));
        return new Location(plugin.world, point[0], point[1], point[2]);
    }


    public boolean isInitialized() {
        return initialized;
    }

    public void deInitialize() {
        this.initialized = false;
    }
}
