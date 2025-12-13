package sbs.immovablerod.metaDungeon.signals;

import sbs.immovablerod.metaDungeon.game.MonsterSpawn;

public class MonsterSpawnResolvedSignal extends BaseSignal {

    private final MonsterSpawn monsterSpawn;

    public MonsterSpawnResolvedSignal(MonsterSpawn monsterSpawn) {
        super();
        this.monsterSpawn = monsterSpawn;
    }

    public MonsterSpawn getMonsterSpawn() {
        return monsterSpawn;
    }
}
