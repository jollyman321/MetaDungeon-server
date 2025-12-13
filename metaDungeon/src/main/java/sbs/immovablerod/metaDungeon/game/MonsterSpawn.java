package sbs.immovablerod.metaDungeon.game;

import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.enums.Signal;
import sbs.immovablerod.metaDungeon.signals.MonsterSpawnInitialisedSignal;
import sbs.immovablerod.metaDungeon.signals.MonsterSpawnResolvedSignal;

import java.util.ArrayList;

import static java.lang.Math.round;
import static sbs.immovablerod.metaDungeon.game.GConfig.*;
import static sbs.immovablerod.metaDungeon.util.Random.getRandInt;

public class MonsterSpawn {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final int monsterCount;
    private final int minLevel;
    private final int maxLevel;
    private boolean resolved;
    private final ArrayList<MetaDungeonMonster> monsters;

    public MonsterSpawn(int monsterCount, int minLevel, int maxLevel) {
        this.monsterCount = monsterCount;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.monsters = new ArrayList<>();
        this.resolved = false;

        signalListeners.get(Signal.MONSTER_SPAWN_INITIALISED).trigger(new MonsterSpawnInitialisedSignal(this));
    }

    public void execute() {
        int _failedLocationCount = 0;
        for (int i = 0; i < this.monsterCount; i++) {
            Location location = GConfig.mapManager.getEntityLocation(false);
            if (location == null) {
                _failedLocationCount++;
                continue;
            }

            String selection = GConfig.monsterSelection.fetch(monsterRarityCategorySelector.resolveRoll());
            while (plugin.jsonLoader.entityTemplates.path(selection).path("minLevel").asInt(1) > this.minLevel) {
                selection = GConfig.monsterSelection.fetch(monsterRarityCategorySelector.resolveRoll());
            }

            this.monsters.add(GConfig.entityManager.add(selection, location, getRandInt(this.minLevel, this.maxLevel)));
        }
        this.resolved = true;
        signalListeners.get(Signal.MONSTER_SPAWN_RESOLVED).trigger(new MonsterSpawnResolvedSignal(this));

        plugin.getLogger().info("spawned '" + (this.monsterCount - _failedLocationCount)  + "' monsters");
        plugin.getLogger().info("target count=" + this.monsterCount + " failed locations=" + _failedLocationCount);

    }

    public ArrayList<MetaDungeonMonster> getMonsters() {
        return monsters;
    }

    public boolean isResolved() {
        return resolved;
    }
}
