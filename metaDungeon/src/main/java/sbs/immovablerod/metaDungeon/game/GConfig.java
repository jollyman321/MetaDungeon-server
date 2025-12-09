package sbs.immovablerod.metaDungeon.game;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.managers.*;
import sbs.immovablerod.metaDungeon.util.JsonLoader;
import sbs.immovablerod.metaDungeon.util.LoadSqlTable;
import sbs.immovablerod.metaDungeon.util.LootTable;

import java.util.HashMap;
import java.util.List;

public class GConfig {
    // stores unchanging game parameters
    private static final MetaDungeon plugin = MetaDungeon.getInstance();

    public static final int roundDuration = plugin.jsonLoader.gameplay.at("/rounds/duration").asInt(120);
    public static final double roundInterWaveScale = plugin.jsonLoader.gameplay.at("/rounds/interWaveScale").asDouble(0.5);
    public static final int roundWaves = plugin.jsonLoader.gameplay.at("/rounds/waves").asInt(3);
    public static final int roundMonsterLevelScaling = plugin.jsonLoader.gameplay.at("/rounds/monsterLevelScaling").asInt(1);
    public static final int roundGracePeriod = plugin.jsonLoader.gameplay.at("/rounds/gracePeriod").asInt(10);

    public static final int monsterDensity = plugin.jsonLoader.gameplay.at("/monsters/density").asInt(700);


    public static final int chestDensity = plugin.jsonLoader.gameplay.at("/chests/density").asInt(1500);
    public static final long chestLifespan = plugin.jsonLoader.gameplay.at("/chests/lifeSpan").asInt(180);
    public static final int chestItemsMin = plugin.jsonLoader.gameplay.at("/chests/itemCountMin").asInt(1);
    public static final int chestItemsMax = plugin.jsonLoader.gameplay.at("/chests/itemCountMax").asInt(5);
    public static final int chestSpawnTimeMin = plugin.jsonLoader.gameplay.at("/chests/spawnTimeMin").asInt(120);
    public static final int chestSpawnTimeMax = plugin.jsonLoader.gameplay.at("/chests/spawnTimeMax").asInt(180);
    public static final int worldEventSpawnTimeMin = plugin.jsonLoader.gameplay.at("/events/spawnTimeMax").asInt(60);
    public static final int worldEventSpawnTimeMax = plugin.jsonLoader.gameplay.at("/events/spawnTimeMax").asInt(180);

    public static final double multiplayerScalingMonsterCount = plugin.jsonLoader.gameplay.at("/multiplayerScaling/monsterCount").asDouble(0.2);
    public static final double multiplayerScalingChestCount = plugin.jsonLoader.gameplay.at("/multiplayerScaling/monsterCount").asDouble(0.2);

    public static final LootTable eventSelection = new LootTable(plugin.jsonLoader.eventTemplates);
    public static final LootTable itemsSelection = new LootTable(plugin.jsonLoader.itemsV2);
    public static final LootTable monsterSelection = new LootTable(plugin.jsonLoader.entityTemplates);

    public static final EventManager eventManager = new EventManager();
    public static final EntityManager entityManager = new EntityManager();
    public static final MessageManager messageManager = new MessageManager();
    public static final SoundManager soundManager = new SoundManager();
    public static final PlayerManager playerManager = new PlayerManager();
    public static final ItemManager itemManager = new ItemManager();
    public static final MapManager mapManager = new MapManager();

    public static void init() {
        itemsSelection.generateMappings("tier", "rarity", "category");
        monsterSelection.generateMappings("rarity");
        eventSelection.generateMappings("rarity", "category");
    }

}
