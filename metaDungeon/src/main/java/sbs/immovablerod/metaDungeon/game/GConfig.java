package sbs.immovablerod.metaDungeon.game;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.util.LoadSqlTable;
import sbs.immovablerod.metaDungeon.util.LootTable;

import java.util.HashMap;
import java.util.List;

public class GConfig {
    // stores unchanging game parameters
    private static final MetaDungeon plugin = MetaDungeon.getInstance();

    public static final int roundDuration = plugin.gameplay.at("/rounds/duration").asInt(120);
    public static final double roundInterWaveScale = plugin.gameplay.at("/rounds/interWaveScale").asDouble(0.5);
    public static final int roundWaves = plugin.gameplay.at("/rounds/waves").asInt(3);
    public static final int roundMonsterLevelScaling = plugin.gameplay.at("/rounds/monsterLevelScaling").asInt(1);
    public static final int roundGracePeriod = plugin.gameplay.at("/rounds/gracePeriod").asInt(10);

    public static final int monsterDensity = plugin.gameplay.at("/monsters/density").asInt(700);


    public static final int chestDensity = plugin.gameplay.at("/chests/density").asInt(1500);
    public static final long chestLifespan = plugin.getConfig().getLong("chest-lifespan");
    public static final int chestItemsMin = plugin.getConfig().getInt("chest-items-min");
    public static final int chestItemsMax = plugin.getConfig().getInt("chest-items-max");
    public static final int chestSpawnTimeMin = plugin.getConfig().getInt("chest-spawn-time-min");
    public static final int chestSpawnTimeMax = plugin.getConfig().getInt("chest-spawn-time-max");
    public static final int worldEventSpawnTimeMin = plugin.getConfig().getInt("worldevent-spawn-time-min");
    public static final int worldEventSpawnTimeMax = plugin.getConfig().getInt("worldevent-spawn-time-max");

    public static final HashMap<String, HashMap<String, HashMap<String, List<String>>>>
            worldEventSelection = LoadSqlTable.tableToNestedHashMap(plugin.worldEventsDB, "tier", "rarity", "category");

    public static final LootTable itemsSelection = new LootTable(plugin.itemsV2);
    public static final LootTable monsterSelection = new LootTable(plugin.entityTemplates);

    public static void init() {
        itemsSelection.generateMappings("tier", "rarity", "category");
        monsterSelection.generateMappings("rarity");
    }

}
