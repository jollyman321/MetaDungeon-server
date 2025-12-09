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

import java.util.*;

import static java.lang.Math.floorDiv;
import static java.lang.Math.round;
import static sbs.immovablerod.metaDungeon.game.GConfig.eventManager;
import static sbs.immovablerod.metaDungeon.game.GConfig.messageManager;
import static sbs.immovablerod.metaDungeon.util.Random.*;


public class Game {
    // stores access to core plugin data
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    // TODO: move to GConfig
    private final WeightedSelection itemRarityCategorySelector = new WeightedSelection(
            plugin.jsonLoader.gameplay.at("/items/rarityDistribution"),
            plugin.jsonLoader.gameplay.at("/items/categoryDistribution")
    );

    private final WeightedSelection eventRarityCategory = new WeightedSelection(
            plugin.jsonLoader.gameplay.at("/events/rarityDistribution"),
            plugin.jsonLoader.gameplay.at("/events/categoryDistribution")
    );

    private final WeightedSelection monsterRarityCategorySelector = new WeightedSelection(
            plugin.jsonLoader.gameplay.at("/monsters/rarityDistribution")
    );

    // *** run time settings ***
    public int currentRound;

    public void startGame(String mapName) {
        GConfig.init();
        GConfig.mapManager.setCurrentMap(mapName);

        if (GConfig.mapManager.isInitialized()) {
            GConfig.playerManager.addPlayersFromWorld();
            GConfig.playerManager.reload();

            Location spawnLocation = GConfig.mapManager.getPlayerSpawnPoint();

            for (MetaDungeonPlayer player : GConfig.playerManager.getAll()) {
                // reload player stats to base state
                player.getPlayer().teleport(spawnLocation);
                player.getPlayer().getInventory().clear();
                player.getPlayer().setGameMode(GameMode.ADVENTURE);
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false));
                player.setInGame(true);
                GConfig.messageManager.addPlayer(player.getPlayer());

            }
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

            GConfig.messageManager.titleAll("Game Starting!", "", Colors.GREEN, Colors.GREEN);
            GConfig.messageManager.messageAllRepeat("Wave Starting in @time Seconds!", Colors.GREEN,  10);
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::startRound, 20L * 10L));
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genChests, 20L * 3));
            plugin.tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for(MetaDungeonPlayer p : GConfig.playerManager.getAll()) {
                    p.update();
                }
            }, 2L, 2L));
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genWorldEvent, 20L * (getRandInt(GConfig.worldEventSpawnTimeMin, GConfig.worldEventSpawnTimeMax))));
        } else {
            plugin.getLogger().severe("Could not start game (reason: map not initialized)");
        }
    }

    public void startRound() {
        currentRound += 1;

        genEntities(1);

        // sub waves
        long subWaveInterval = floorDiv(GConfig.roundDuration, GConfig.roundWaves - 1);
        for (int i=0;i < GConfig.roundWaves - 1;i++) {
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> genEntities((float) GConfig.roundInterWaveScale), 20L * subWaveInterval * (i+1)));
        }
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::endRound, 20L * GConfig.roundDuration));
    }

    public void endRound() {
        GConfig.messageManager.titleAll("Round " + currentRound + " Complete!", "Prepare for Stronger Monsters", Colors.GREEN, Colors.RED);

        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::startRound, 20L * GConfig.roundGracePeriod));
    }

    public void genEntities(float countModifier) {
        messageManager.messageAll("Monsters approach!", Colors.AQUA);

        // number of entities being spawn on a current wave
        int entityCount = Math.toIntExact(round(((float) plugin.map.getEntityLocations().size() * ((float) GConfig.monsterDensity / 10000)) * countModifier *
                (1.0 + GConfig.multiplayerScalingMonsterCount * (float) (GConfig.playerManager.getAll().size() - 1.0))));

        plugin.getLogger().info("spawning '" + entityCount + "' monsters");

        for (int i = 0; i < entityCount; i++) {
            Location location = GConfig.mapManager.getEntityLocation();


            String selection = GConfig.monsterSelection.fetch(monsterRarityCategorySelector.resolveRoll());
            while (plugin.jsonLoader.entityTemplates.path(selection).path("minLevel").asInt(1) > currentRound) {
                selection = GConfig.monsterSelection.fetch(monsterRarityCategorySelector.resolveRoll());
            }

            GConfig.entityManager.add(selection, location, getRandInt(currentRound, currentRound * 3));

        }
    }

    public void genChests() {
        // chest density is calculated as <valid locations> * (density/10000) * mods

        int chestCount = Math.toIntExact(
                round(((float) plugin.map.getChestLocations().size() * ((float) GConfig.chestDensity / 10000)) *
                (1.0 + GConfig.multiplayerScalingChestCount * ((float) GConfig.playerManager.getAll().size() - 1.0))));

        plugin.getLogger().info("placing " + chestCount + " chests");

        List<Location> locations = new ArrayList<>();

        RandomCollection<String> tierWeights = generateLootTier(currentRound);

        for (int i = 0;i < chestCount;i++) {
            Location location = GConfig.mapManager.getChestLocation();
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
                    MetaDungeonItem item = GConfig.itemManager.add(GConfig.itemsSelection.fetch(itemParams));

                    if (item == null) continue;

                    item.setAmount(getRandInt(item.getTemplate().path("minStackSize").asInt(1),
                                         item.getTemplate().path("maxStackSize").asInt(1)));

                    chest.getInventory().setItem(getRandInt(1, 26), item);

                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] failed to create item");
                    System.out.println(e);
                }
            }
        }
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> Tasks.removeChests(locations), 20L * GConfig.chestLifespan));
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genChests, 20L * (getRandInt(GConfig.chestSpawnTimeMin, GConfig.chestSpawnTimeMax)))
        );
    }

    public void genWorldEvent() {
        String event = GConfig.eventSelection.fetch(eventRarityCategory.resolveRoll());
        eventManager.add(event, currentRound);
        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::genWorldEvent, 20L * (getRandInt(GConfig.worldEventSpawnTimeMin, GConfig.worldEventSpawnTimeMax))));
    }
    public void onPlayerDeath() {
        List<Boolean> dead = new ArrayList<>();
        GConfig.playerManager.getAll().forEach(player -> dead.add(player.isDead()));
        System.out.println("PLAYER DIED");
        System.out.println(dead);
        if (!dead.contains(false)) {
            messageManager.titleAll("Game Over!", "All Players Defeated", Colors.DARK_RED, Colors.RED);
            plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, this::stopGame, 20L * 5));
        }
    }

    public void stopGame() {
        plugin.getLogger().info("stopping game");

        plugin.world.setStorm(false);
        plugin.world.setClearWeatherDuration(1000000);

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
        for (UUID entity : GConfig.entityManager.getAllIDs()) {
            GConfig.entityManager.getFromID(entity).getEntity().remove();
        }
        for (MetaDungeonPlayer player : GConfig.playerManager.getAll()) {
            player.setInGame(false);
        }

        GConfig.entityManager.clear();
        GConfig.messageManager.clear();
        GConfig.eventManager.clear();
        GConfig.itemManager.clear();

    }
}
