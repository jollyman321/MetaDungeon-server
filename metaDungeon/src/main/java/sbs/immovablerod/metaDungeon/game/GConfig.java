package sbs.immovablerod.metaDungeon.game;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.managers.EntityManager;
import sbs.immovablerod.metaDungeon.managers.EventManager;
import sbs.immovablerod.metaDungeon.managers.MessageManager;
import sbs.immovablerod.metaDungeon.managers.SoundManager;
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
    public static final long chestLifespan = plugin.gameplay.at("/chests/lifeSpan").asInt(180);
    public static final int chestItemsMin = plugin.gameplay.at("/chests/itemCountMin").asInt(1);
    public static final int chestItemsMax = plugin.gameplay.at("/chests/itemCountMax").asInt(5);
    public static final int chestSpawnTimeMin = plugin.gameplay.at("/chests/spawnTimeMin").asInt(120);
    public static final int chestSpawnTimeMax = plugin.gameplay.at("/chests/spawnTimeMax").asInt(180);
    public static final int worldEventSpawnTimeMin = plugin.gameplay.at("/events/spawnTimeMax").asInt(60);
    public static final int worldEventSpawnTimeMax = plugin.gameplay.at("/events/spawnTimeMax").asInt(180);

    public static final double multiplayerScalingMonsterCount = plugin.gameplay.at("/multiplayerScaling/monsterCount").asDouble(0.2);
    public static final double multiplayerScalingChestCount = plugin.gameplay.at("/multiplayerScaling/monsterCount").asDouble(0.2);

    public static final LootTable eventSelection = new LootTable(plugin.eventTemplates);
    public static final LootTable itemsSelection = new LootTable(plugin.itemsV2);
    public static final LootTable monsterSelection = new LootTable(plugin.entityTemplates);

    public static final EventManager eventManager = new EventManager();
    public static final EntityManager entityManager = new EntityManager();
    public static final MessageManager messageManager = new MessageManager();
    public static final SoundManager soundManager = new SoundManager();

    public static void init() {
        itemsSelection.generateMappings("tier", "rarity", "category");
        monsterSelection.generateMappings("rarity");
        eventSelection.generateMappings("rarity", "category");
    }

}
