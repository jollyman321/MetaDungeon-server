package sbs.immovablerod.metaDungeon.game;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.util.*;
import sbs.immovablerod.metaDungeon.util.Random;

import java.util.*;

import static java.lang.Math.floorDiv;
import static java.lang.Math.round;
import static sbs.immovablerod.metaDungeon.util.Random.*;


public class Game {
    // stores access to core plugin data
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final RandomCollection<String> worldEventCategorySelection = Random.getRandomCollection((List<List<?>>) plugin.getConfig().getList("worldevent-category-weight"));
    private final RandomCollection<String> worldEventRaritySelection = Random.getRandomCollection((List<List<?>>) plugin.getConfig().getList("worldevent-rarity-weight"));

    private final  WeightedSelection itemRarityCategorySelector = new WeightedSelection(
            plugin.gameplay.at("/items/rarityDistribution"),
            plugin.gameplay.at("/items/categoryDistribution")
    );

    private final  WeightedSelection monsterRarityCategorySelector = new WeightedSelection(
            plugin.gameplay.at("/monsters/rarityDistribution")
    );

    // *** run time settings ***
    private int currentRound;

    public void startGame(String name) {
        GConfig.init();

        Location spawnLocation = new Location(plugin.world,
                Double.parseDouble(plugin.mapsDB.get(name).get("spawnx")),
                Double.parseDouble(plugin.mapsDB.get(name).get("spawny")),
                Double.parseDouble(plugin.mapsDB.get(name).get("spawnz")));
        for (UUID key : plugin.players.keySet()) {
            // reload player stats to base state
            System.out.println(plugin.players);
            plugin.players.put(key, new MetaDungeonPlayer(plugin.players.get(key).getPlayer()));
            System.out.println(plugin.players);
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
        Message.messageAllRepeat("Wave Starting in @time Seconds!", Colors.GREEN,  10);
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::startRound, 20L * 1L));
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genChests, 20L * 3));
        plugin.tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, () -> {for(MetaDungeonPlayer p : plugin.players.values()){p.update();}}, 2L, 2L));

        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genWorldEvent, 20L * (getRandInt(GConfig.worldEventSpawnTimeMin, GConfig.worldEventSpawnTimeMax))));

    }

    public void startRound() {
        currentRound += 1;

        genEntities(1);

        // sub waves
        long subWaveInterval = floorDiv(GConfig.roundDuration, GConfig.roundWaves - 1);
        for (int i=0;i < GConfig.roundWaves - 1;i++) {
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {
                genEntities((float) GConfig.roundInterWaveScale);
                }, 20L * subWaveInterval * (i+1)));
        }
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::endRound, 20L * GConfig.roundDuration));
    }

    public void endRound() {
        Message.titleAll("Round " + currentRound + " Complete!", "Prepare for Stronger Monsters", Colors.GREEN, Colors.RED);

        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::startRound, 20L * GConfig.roundGracePeriod));
    }

    public void genEntities(float countModifier) {
        for (MetaDungeonPlayer player : plugin.players.values()) {
            player.getPlayer().sendMessage("Monsters approach!");
        }

        // number of entities being spawn on a current wave
        int entityCount = Math.round(((float) plugin.map.getEntityLocations().size() / GConfig.monsterDensity) * countModifier);
        plugin.getLogger().info("spawning '" + entityCount + "' monsters");

        for (int i = 0; i < entityCount; i++) {
            Location location = plugin.map.getEntityLocations().get(getRandInt(0, plugin.map.getEntityLocations().size() - 1));


            String selection = GConfig.monsterSelection.fetch(monsterRarityCategorySelector.resolveRoll());
            while (plugin.entityTemplates.path(selection).path("minLevel").asInt(1) > currentRound) {
                System.out.println(selection);
                selection = GConfig.monsterSelection.fetch(monsterRarityCategorySelector.resolveRoll());
            }

            SpawnEntity.spawn(selection,
                    location,
                    getRandInt(currentRound, currentRound * 2));
        }
    }

    public void genChests() {
        int chestCount =  round((float) plugin.map.getChestLocations().size() / GConfig.chestDensity);
        plugin.getLogger().info("placing " + chestCount + " chests");

        List<Location> locations = new ArrayList<>();

        RandomCollection<String> tierWeights = generateLootTier(currentRound);

        for (int i = 0;i < chestCount;i++) {
            Location location = plugin.map.getChestLocations().get(getRandInt(0, plugin.map.getChestLocations().size() - 1));
            locations.add(location);

            Block block = location.getBlock();

            if (!(block.getType().equals(Material.AIR))) continue;

            block.setType(Material.CHEST);
            Chest chest = (Chest) block.getState();

            int itemCount = getRandInt(GConfig.chestItemsMin, GConfig.chestItemsMax);
            for (int j = 1;j < itemCount+1; j++) {

                try {
                    List<String> itemParams = itemRarityCategorySelector.resolveRoll();
                    itemParams.add(0, tierWeights.next());
                    MetaDungeonItem item = ItemUtil.createItem(GConfig.itemsSelection.fetch(itemParams));

                    if (item == null) continue;

                    List<Integer> stackSize = plugin.getConfig().getIntegerList("item-stack-sizes." + item.category);
                    item.setAmount(getRandInt(stackSize.get(0), stackSize.get(1)));
                    chest.getInventory().setItem(getRandInt(1, 26), item);

                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] failed to create item");
                    System.out.println(e);
                }
            }
        }
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {Tasks.removeChests(locations);}, 20L * GConfig.chestLifespan));
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genChests, 20L * (getRandInt(GConfig.chestSpawnTimeMin, GConfig.chestSpawnTimeMax)))
        );
    }

    public void genWorldEvent() {
        RandomCollection<String> worldEventTierSelection = Random.getRandomCollection((List<List<?>>) plugin.getConfig().getList("waves." + "one" + ".worldevent-tier-weight"));

        List<String> events = GConfig.worldEventSelection.get(worldEventTierSelection.next()).get(worldEventRaritySelection.next()).get(worldEventCategorySelection.next());
        String event = events.get(getRandInt(0, events.size() - 1));
        WorldEvents.triggerEvent(this, event);
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genWorldEvent, 20L * (getRandInt(GConfig.worldEventSpawnTimeMin, GConfig.worldEventSpawnTimeMax))));
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
