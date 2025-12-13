package sbs.immovablerod.metaDungeon.game;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.enums.Signal;
import sbs.immovablerod.metaDungeon.managers.*;
import sbs.immovablerod.metaDungeon.signals.SignalListener;
import sbs.immovablerod.metaDungeon.util.LootTable;
import sbs.immovablerod.metaDungeon.util.WeightedSelection;

import java.util.HashMap;

public class GConfig {
    // stores unchanging game parameters
    private static final MetaDungeon plugin = MetaDungeon.getInstance();

    public static final int roundDuration = plugin.jsonLoader.gameplay.at("/rounds/duration").asInt(120);
    public static final double roundInterWaveScale = plugin.jsonLoader.gameplay.at("/rounds/interWaveScale").asDouble(0.5);
    public static final int roundWaves = plugin.jsonLoader.gameplay.at("/rounds/waves").asInt(3);
    public static final int roundMonsterLevelScaling = plugin.jsonLoader.gameplay.at("/rounds/monsterLevelScaling").asInt(1);
    public static final int roundGracePeriod = plugin.jsonLoader.gameplay.at("/rounds/gracePeriod").asInt(10);

    public static final int monsterDensity = plugin.jsonLoader.gameplay.at("/monsters/density").asInt(2);
    public static final int monsterBaseFollowRange = plugin.jsonLoader.gameplay.at("/monsters/baseFollowRange").asInt(20);
    public static final int monsterMinSpawnRadius = plugin.jsonLoader.gameplay.at("/monsters/minSpawnRadius").asInt(5);
    public static final int monsterMaxSpawnRadius = plugin.jsonLoader.gameplay.at("/monsters/maxSpawnRadius").asInt(-1);

    public static final int chestDensity = plugin.jsonLoader.gameplay.at("/chests/density").asInt(2);
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

    public static WeightedSelection itemRarityCategorySelector;

    public static WeightedSelection eventRarityCategory;

    public static WeightedSelection monsterRarityCategorySelector;


    public static final EventManager eventManager = new EventManager();
    public static final EntityManager entityManager = new EntityManager();
    public static final MessageManager messageManager = new MessageManager();
    public static final SoundManager soundManager = new SoundManager();
    public static final PlayerManager playerManager = new PlayerManager();
    public static final ItemManager itemManager = new ItemManager();
    public static final MapManager mapManager = new MapManager();
    public static final TaskManager taskManager = new TaskManager();

    public static final HashMap<Signal, SignalListener> signalListeners = new HashMap<>();

    public static void init() {
        System.out.println(chestDensity);
        itemsSelection.generateMappings("tier", "rarity", "category");
        monsterSelection.generateMappings("rarity");
        eventSelection.generateMappings("rarity", "category");

        signalListeners.put(Signal.PLAYER_RIGHT_CLICK, new SignalListener());
        signalListeners.put(Signal.NEW_QUEST, new SignalListener());
        signalListeners.put(Signal.QUEST_COMPLETED, new SignalListener());
        signalListeners.put(Signal.MONSTER_SPAWN_INITIALISED, new SignalListener());
        signalListeners.put(Signal.MONSTER_SPAWN_RESOLVED, new SignalListener());

        itemRarityCategorySelector = new WeightedSelection(
                plugin.jsonLoader.gameplay.at("/items/rarityDistribution"),
                plugin.jsonLoader.gameplay.at("/items/categoryDistribution")
        );

        eventRarityCategory = new WeightedSelection(
                plugin.jsonLoader.gameplay.at("/events/rarityDistribution"),
                plugin.jsonLoader.gameplay.at("/events/categoryDistribution")
        );

        monsterRarityCategorySelector = new WeightedSelection(
                plugin.jsonLoader.gameplay.at("/monsters/rarityDistribution")
        );
    }

}
