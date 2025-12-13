package sbs.immovablerod.metaDungeon.game;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.signals.SignalListener;
import sbs.immovablerod.metaDungeon.util.*;

import java.util.*;

import static java.lang.Math.floorDiv;
import static java.lang.Math.round;
import static sbs.immovablerod.metaDungeon.game.GConfig.*;
import static sbs.immovablerod.metaDungeon.util.Random.*;


public class Runtime {
    private static final Logger log = LogManager.getLogger(Runtime.class);
    // stores access to core plugin data
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    // *** run time settings ***
    public int currentRound;
    private Location spawnLocation;

    public void startGame(String mapName) {
        GConfig.init();
        GConfig.mapManager.setCurrentMap(mapName);

        if (GConfig.mapManager.isInitialized()) {
            GConfig.playerManager.addPlayersFromWorld();
            GConfig.playerManager.reload();

            this.spawnLocation = GConfig.mapManager.getPlayerSpawnPoint();

            for (MetaDungeonPlayer player : GConfig.playerManager.getAll()) {
                // reload player stats to base state
                player.getPlayer().teleport(this.spawnLocation);
                player.getPlayer().getInventory().clear();
                player.getPlayer().setGameMode(GameMode.ADVENTURE);
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, true, false));
                player.getPlayer().getInventory().setItem(8, itemManager.add("questCompass", player));
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

            GConfig.messageManager.titleAll("Runtime Starting!", "", Colors.GREEN, Colors.GREEN);
            GConfig.messageManager.messageAllRepeat("Wave Starting in @time Seconds!", Colors.GREEN,  10);
            taskManager.runTaskLater(this::startRound, 20L * 10L);
            taskManager.runTaskLater(this::genChests, 20L * 3);
            taskManager.runTaskTimer(() -> {
                for(MetaDungeonPlayer p : GConfig.playerManager.getAll()) {
                    p.update();
                }
            }, 2L, 2L);
            taskManager.runTaskLater(this::genWorldEvent, 20L * (getRandInt(GConfig.worldEventSpawnTimeMin, GConfig.worldEventSpawnTimeMax)));
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
            taskManager.runTaskLater(() -> genEntities((float) GConfig.roundInterWaveScale), 20L * subWaveInterval * (i+1));
        }
        taskManager.runTaskLater(this::endRound, 20L * GConfig.roundDuration);
    }

    public void endRound() {
        GConfig.messageManager.titleAll("Round " + currentRound + " Complete!", "Prepare for Stronger Monsters", Colors.GREEN, Colors.RED);

        taskManager.runTaskLater(this::startRound, 20L * GConfig.roundGracePeriod);
    }

    public void genEntities(float countModifier) {
        messageManager.messageAll("Monsters approach!", Colors.AQUA);
        System.out.println(GConfig.monsterDensity + "md");
        // number of entities being spawn on a current wave
        int entityCount = Math.toIntExact(round(
                        ((float) GConfig.mapManager.getAllEntityLocations().size() * ((float) GConfig.monsterDensity / (float) 10000.0))
                        * countModifier
                        // handle map based scaling
                        *(float)  GConfig.mapManager.getMonsterDensityModifier()
                        // Handles multiplayer scaling
                        * (float) ((float) 1.0 + GConfig.multiplayerScalingMonsterCount * (float) (GConfig.playerManager.getAll().size() - 1.0))
        ));

        new MonsterSpawn(entityCount, this.currentRound, this.currentRound * 3).execute();


    }

    public void genChests() {
        // chest density is calculated as <valid locations> * (density/10000) * mods
        System.out.println( GConfig.chestDensity + "md");
        System.out.println(GConfig.mapManager.getChestDensityModifier());
        int chestCount = Math.toIntExact(round(
                        ((float) GConfig.mapManager.getAllChestLocations().size() * ((float) GConfig.chestDensity / (float) 10000.0))
                        * (float) GConfig.mapManager.getChestDensityModifier()
                        * (float) (1.0 + GConfig.multiplayerScalingChestCount * ((float) GConfig.playerManager.getAll().size() - 1.0))
        ));

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
                    List<String> itemParams = GConfig.itemRarityCategorySelector.resolveRoll();
                    itemParams.addFirst(tierWeights.next());
                    MetaDungeonItem item = GConfig.itemManager.add(GConfig.itemsSelection.fetch(itemParams), null);

                    if (item == null) continue;

                    item.setAmount(getRandInt(item.getTemplate().path("minStackSize").asInt(1),
                                         item.getTemplate().path("maxStackSize").asInt(1)));

                    chest.getInventory().setItem(getRandInt(1, 26), item);

                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] failed to create item");
                    log.error("e: ", e);
                }
            }
        }
        taskManager.runTaskLater(() -> Tasks.removeChests(locations), 20L * GConfig.chestLifespan);
        taskManager.runTaskLater(this::genChests, 20L * (getRandInt(GConfig.chestSpawnTimeMin, GConfig.chestSpawnTimeMax)));
    }

    public void genWorldEvent() {
        String event = GConfig.eventSelection.fetch(GConfig.eventRarityCategory.resolveRoll());
        eventManager.add(event, currentRound);
        taskManager.runTaskLater(this::genWorldEvent, 20L * (getRandInt(GConfig.worldEventSpawnTimeMin, GConfig.worldEventSpawnTimeMax)));
    }
    public void onPlayerDeath() {
        List<Boolean> dead = new ArrayList<>();
        GConfig.playerManager.getAll().forEach(player -> dead.add(player.isDead()));
        System.out.println("PLAYER DIED");
        System.out.println(dead);
        if (!dead.contains(false)) {
            messageManager.titleAll("Runtime Over!", "All Players Defeated", Colors.DARK_RED, Colors.RED);
            taskManager.runTaskLater(this::stopGame, 20L * 5);
        }
    }

    public void stopGame() {
        plugin.getLogger().info("stopping game");

        plugin.world.setStorm(false);
        plugin.world.setClearWeatherDuration(1000000);

        for (Location location : GConfig.mapManager.getAllChestLocations()) {
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
            player.getPlayer().getInventory().clear();
        }

        signalListeners.values().forEach(SignalListener::clearTasks);

        GConfig.entityManager.clear();
        GConfig.messageManager.clear();
        GConfig.eventManager.clear();
        GConfig.itemManager.clear();
        taskManager.clear();


    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public int getCurrentRound() {
        return currentRound;
    }
}
