package sbs.immovablerod.metaDungeon.game;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import sbs.immovablerod.metaDungeon.Enums.Colors;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.util.*;
import sbs.immovablerod.metaDungeon.util.Random;

import java.util.*;

import static java.lang.Math.floorDiv;
import static java.lang.Math.round;
import static sbs.immovablerod.metaDungeon.util.Random.getRandomCollection;
import static sbs.immovablerod.metaDungeon.util.Random.resolveSelection;


public class Game {
    // stores access to core plugin data
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final RandomCollection<String> worldEventCategorySelection = Random.getRandomCollection((List<List<?>>) plugin.getConfig().getList("worldevent-category-weight"));
    private final RandomCollection<String> worldEventRaritySelection = Random.getRandomCollection((List<List<?>>) plugin.getConfig().getList("worldevent-rarity-weight"));
    // *** run time settings ***

    private int currentWaveSequence = 0;
    private String currentWaveId = GConfig.waveSequence.get(0);
    private int waveDensity;

    // a weighted selection of the current entities
    private RandomCollection<String> entitySelection;
    // number of entities being spawn on a current wave
    private int entityCount;
    public void startGame(String name) {
        Location spawnLocation = new Location(plugin.world,
                GConfig.spawnLocationCoords.get(0),
                GConfig.spawnLocationCoords.get(1),
                GConfig.spawnLocationCoords.get(2), 0, 0);

        for (UUID key : plugin.players.keySet()) {
            // reload player stats to base state
            plugin.players.put(key, new MetaDungeonPlayer(plugin.players.get(key).getPlayer()));
            plugin.players.get(key).getPlayer().teleport(spawnLocation);
            plugin.players.get(key).getPlayer().getInventory().clear();
            plugin.players.get(key).getPlayer().setGameMode(GameMode.ADVENTURE);
            plugin.players.get(key).getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false));
            plugin.players.get(key).setInGame(true);

            plugin.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            plugin.world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            plugin.world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            plugin.world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            plugin.world.setGameRule(GameRule.DO_LIMITED_CRAFTING, true);
            plugin.world.setGameRule(GameRule.DO_MOB_LOOT, false);
            plugin.world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            plugin.world.setGameRule(GameRule.DO_TILE_DROPS, false);
            plugin.world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            plugin.world.setGameRule(GameRule.KEEP_INVENTORY, true);
            plugin.world.setGameRule(GameRule.NATURAL_REGENERATION, false);

            plugin.world.setGameRule(GameRule.MOB_GRIEFING, false);
            plugin.world.setGameRule(GameRule.DO_FIRE_TICK, false);

        }
        Message.titleAll("Game Starting!", "", Colors.GREEN, Colors.GREEN);
        Message.messageAllRepeat("Wave Starting in @time Seconds!", Colors.GREEN,  (int) GConfig.waveStartGracePeriod);
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::startWave, 20L * GConfig.waveStartGracePeriod));
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genChests, 20L * GConfig.chestSpawnGracePeriod));
        plugin.tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, () -> {for(MetaDungeonPlayer p : plugin.players.values()){p.update();}}, 2L, 2L));
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genWorldEvent, 20L * (Random.getRandInt(GConfig.worldEventSpawnTimeMin, GConfig.worldEventSpawnTimeMax))));

    }

    public void startWave() {

        List<List<?>> waveSpawns = (List<List<?>>) plugin.getConfig().getList("waves." + currentWaveId + ".spawns");
        int subWaves = plugin.getConfig().getInt("waves." + currentWaveId + ".sub-waves");
        int waveDuration = plugin.getConfig().getInt("waves." + currentWaveId + ".duration");
        waveDensity = plugin.getConfig().getInt("waves." + currentWaveId + ".density");
        entityCount =  round((float) plugin.map.getEntityLocations().size() / waveDensity);
        entitySelection = getRandomCollection(waveSpawns);

        genEntities(1);
        // sub waves
        long subWaveInterval = floorDiv(waveDuration, subWaves + 1);
        for (int i=0;i<subWaves;i++) {
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {genEntities((float) 0.25);}, 20L * subWaveInterval * (i+1)));
        }
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::endWave, 20L * waveDuration));
    }

    public void endWave() {
        Message.titleAll("Wave " + currentWaveId + " Complete!", "Prepare for Stronger Monsters", Colors.GREEN, Colors.RED);
        currentWaveSequence += 1;
        currentWaveId = GConfig.waveSequence.get(currentWaveSequence);
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::startWave, 20L * GConfig.interWaveGracePeriod));

    }

    public void genEntities(float countModifier) {
        RandomCollection<String> monsterTierSelection = getRandomCollection((List<List<?>>) plugin.getConfig().getList("waves." + currentWaveId + ".monster-tier-weight"));
        for (MetaDungeonPlayer player : plugin.players.values()) {
            player.getPlayer().sendMessage("Monsters approach!");
        }
        entityCount =  round(((float) plugin.map.getEntityLocations().size() / waveDensity) * countModifier);
        System.out.println("spawning " + entityCount + " entities");
        for (int i = 0;i < entityCount;i++) {
            Location location = plugin.map.getEntityLocations().get(Random.getRandInt(0, plugin.map.getEntityLocations().size() - 1));
            SpawnEntity.spawn(resolveSelection("monster",  monsterTierSelection.next(), "1", GConfig.monsterSelection), location);
        }
    }

    public void genChests() {
        List<List<?>> lootCategoryWeight = (List<List<?>>) plugin.getConfig().getList("waves." + currentWaveId + ".loot-category-weight");
        List<List<?>> lootTierWeight = (List<List<?>>) plugin.getConfig().getList("waves." + currentWaveId + ".loot-tier-weight");
        List<List<?>> lootRarityWeight = (List<List<?>>) plugin.getConfig().getList("item-rarity-weight");

        RandomCollection<String> lootCategorySelection = getRandomCollection(lootCategoryWeight);
        RandomCollection<String> lootTierSelection = getRandomCollection(lootTierWeight);
        RandomCollection<String> lootRaritySelection = getRandomCollection(lootRarityWeight);

        int chestCount =  round((float) plugin.map.getChestLocations().size() / GConfig.chestDensity);
        plugin.getLogger().info("placing " + chestCount + " chests");


        List<Location> locations = new ArrayList<>();
        for (int i = 0;i < chestCount;i++) {
            Location location = plugin.map.getChestLocations().get(Random.getRandInt(0, plugin.map.getChestLocations().size() - 1));
            locations.add(location);

            Block block =  location.getBlock();

            if (!(block.getType().equals(Material.AIR))) continue;
            block.setType(Material.CHEST);
            Chest chest = (Chest) block.getState();

            int itemCount = Random.getRandInt(GConfig.chestItemsMin, GConfig.chestItemsMax);
            for (int j = 1;j < itemCount+1; j++) {
                String category = lootCategorySelection.next();
                List<Integer> stackSize = plugin.getConfig().getIntegerList("item-stack-sizes." + category);
                try {
                    MetaDungeonItem item = ItemUtil.createItem(resolveSelection(category,  lootTierSelection.next(), lootRaritySelection.next(), GConfig.leveledLootSelection));
                    if (item == null) continue;
                    item.setAmount(Random.getRandInt(stackSize.get(0), stackSize.get(1)));
                    chest.getInventory().setItem(Random.getRandInt(1, 26), item);
                } catch (IllegalArgumentException ignore) {
                    System.out.println("[WARN] failed to create item");
                }
            }
        }
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {Tasks.removeChests(locations);}, 20L * GConfig.chestLifespan));
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genChests, 20L * (Random.getRandInt(GConfig.chestSpawnTimeMin, GConfig.chestSpawnTimeMax)))
        );
    }

    public void genWorldEvent() {
        RandomCollection<String> worldEventTierSelection = Random.getRandomCollection((List<List<?>>) plugin.getConfig().getList("waves." + currentWaveId + ".worldevent-tier-weight"));

        List<String> events = GConfig.worldEventSelection.get(worldEventCategorySelection.next()).get(worldEventTierSelection.next()).get(worldEventRaritySelection.next());
        String event = events.get(Random.getRandInt(0, events.size() - 1));
        WorldEvents.triggerEvent(this, event);
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genWorldEvent, 20L * (Random.getRandInt(GConfig.worldEventSpawnTimeMin, GConfig.worldEventSpawnTimeMax))));
    }

    public void stopGame() {
        System.out.println("stopping game");
        for (BukkitTask task : plugin.tasks) {
            task.cancel();
        }

        for (Location location : plugin.map.getChestLocations()) {
            try {
                Block block = location.getBlock();
                if (block.getType().equals(Material.CHEST)) {
                    Chest chest = (Chest) block.getState();
                    chest.getInventory().clear();
                }
                block.setType(Material.AIR);
            } catch (NullPointerException ignored) {}
        }
        for (UUID entity : plugin.entities.keySet()) {
            plugin.entities.get(entity).getEntity().remove();
        }
        for (MetaDungeonPlayer player : plugin.players.values()) {
            player.setInGame(false);
        }
        plugin.entities.clear();
        plugin.items.clear();
    }
}
