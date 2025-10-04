package sbs.immovablerod.metaDungeon.game;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.util.LoadSqlTable;

import java.util.HashMap;
import java.util.List;

public class GConfig {
    // stores unchanging game parameters
    private static final MetaDungeon plugin = MetaDungeon.getInstance();

    public static final List<String> waveSequence = plugin.getConfig().getStringList("wave-sequence");
    public static final long waveStartGracePeriod = plugin.getConfig().getLong("wave-start-grace-peroid");
    public static final long interWaveGracePeriod = plugin.getConfig().getLong("interwave-grace-peroid");
    public static final List<Integer> spawnLocationCoords = plugin.getConfig().getIntegerList("player-spawn");

    public static final int chestDensity = plugin.getConfig().getInt("chest-density");
    public static final long chestLifespan = plugin.getConfig().getLong("chest-lifespan");
    public static final int chestItemsMin = plugin.getConfig().getInt("chest-items-min");
    public static final int chestItemsMax = plugin.getConfig().getInt("chest-items-max");
    public static final int chestSpawnTimeMin = plugin.getConfig().getInt("chest-spawn-time-min");
    public static final int chestSpawnTimeMax = plugin.getConfig().getInt("chest-spawn-time-max");
    public static final long chestSpawnGracePeriod = plugin.getConfig().getLong("chest-spawn-grace-peroid");

    public static final int worldEventSpawnTimeMin = plugin.getConfig().getInt("worldevent-spawn-time-min");
    public static final int worldEventSpawnTimeMax = plugin.getConfig().getInt("worldevent-spawn-time-max");

    public static final HashMap<String, HashMap<String, HashMap<String, List<String>>>>
            leveledLootSelection = LoadSqlTable.tableToNestedHashMap(plugin.itemsDB, "category", "tier", "rarity");
    public static final HashMap<String, HashMap<String, HashMap<String, List<String>>>>
            worldEventSelection = LoadSqlTable.tableToNestedHashMap(plugin.worldEventsDB, "category", "tier", "rarity");
    public static final HashMap<String, HashMap<String, HashMap<String, List<String>>>>
            monsterSelection = LoadSqlTable.tableToNestedHashMap(plugin.entitiesDB, "category", "tier", "rarity");
}
