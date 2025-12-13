package sbs.immovablerod.metaDungeon.managers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.game.GConfig;
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
    private List<Location> chestLocations;
    private List<Location> entityLocations;
    private List<Location> oversizedEntityLocations;
    private List<Location> playerSpawnPoints;
    private int monsterMinSpawnRadius;
    private int monsterMaxSpawnRadius;

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
                chestLocationsRaw.forEach(
                        loc -> this.chestLocations.add(new Location(plugin.world, loc.get(0).asInt(), loc.get(1).asInt(), loc.get(2).asInt())));
            }

            final JsonNode entityLocationsRaw = mapData.get("entityLocations");
            if (entityLocationsRaw.isArray()) {
                entityLocationsRaw.forEach(
                        loc -> this.entityLocations.add(new Location(plugin.world, loc.get(0).asInt(), loc.get(1).asInt(), loc.get(2).asInt())));
            }

            final JsonNode oversizedEntityLocationsRaw = mapData.get("oversizedEntityLocations");
            if (oversizedEntityLocationsRaw.isArray()) {
                oversizedEntityLocationsRaw.forEach(
                        loc -> this.oversizedEntityLocations.add(new Location(plugin.world, loc.get(0).asInt(), loc.get(1).asInt(), loc.get(2).asInt())));
            }

            final JsonNode playerSpawnPointsRaw = plugin.jsonLoader.mapTemplates.path(this.mapName).path("playerSpawnPoints");
            if (playerSpawnPointsRaw.isArray()) {
                playerSpawnPointsRaw.forEach(
                        loc -> this.playerSpawnPoints.add(new Location(plugin.world, loc.get(0).asInt(), loc.get(1).asInt(), loc.get(2).asInt())));
            }

            if (this.currentMap.at("/gameplay/monsters/minSpawnRadius").asInt(0) != 0) {
                this.monsterMinSpawnRadius = this.currentMap.at("/gameplay/monsters/minSpawnRadius").asInt();
            } else {
                this.monsterMinSpawnRadius = GConfig.monsterMinSpawnRadius;
            }

            if (this.currentMap.at("/gameplay/monsters/maxSpawnRadius").asInt(0) != 0) {
                this.monsterMaxSpawnRadius = this.currentMap.at("/gameplay/monsters/maxSpawnRadius").asInt();
            } else {
                this.monsterMaxSpawnRadius = GConfig.monsterMaxSpawnRadius;
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
        return this.chestLocations.get(Random.getRandInt(0, this.chestLocations.size() - 1));
    }

    public Location getEntityLocation(boolean force) {
        // if force is enabled this function will recursively attempt to find a valid location
        Location location = this.entityLocations.get(Random.getRandInt(0, this.entityLocations.size() - 1));

        for (MetaDungeonPlayer player : GConfig.playerManager.getAll()) {
            if (this.monsterMaxSpawnRadius > -1) {
                if (!player.isDead() && player.isInGame()) {
                    if (player.getPlayer().getLocation().distance(location) < this.monsterMaxSpawnRadius
                    &&  player.getPlayer().getLocation().distance(location) > this.monsterMinSpawnRadius) {
                        return location;
                    } else if (force) {
                        // possible recursion error with this method
                        return this.getEntityLocation(true);
                    } else {
                        return null;
                    }
                }
            } else if (player.getPlayer().getLocation().distance(location) > this.monsterMinSpawnRadius) {
                return location;
            } else if (force) {
                // possible recursion error with this method
                return this.getEntityLocation(true);
            } else {
                return null;
            }
        }

        return null;
    }

    public Location getPlayerSpawnPoint() {
        return this.playerSpawnPoints.get(Random.getRandInt(0, this.playerSpawnPoints.size() - 1));
    }


    public boolean isInitialized() {
        return initialized;
    }

    public void deInitialize() {
        this.initialized = false;
    }

    public List<Location> getAllChestLocations() {
        return chestLocations;
    }
    public List<Location> getAllEntityLocations() {
        return entityLocations;
    }

    public double getChestDensityModifier() {
        return this.currentMap.at("/gameplay/chests/density").asDouble(1.0);
    }
    public double getMonsterDensityModifier() {
        return this.currentMap.at("/gameplay/monsters/density").asDouble(1.0);
    }
    public double getBaseFollowRangeModifier() {
        return this.currentMap.at("/gameplay/monsters/baseFollowRange").asDouble(1.0);
    }
}
