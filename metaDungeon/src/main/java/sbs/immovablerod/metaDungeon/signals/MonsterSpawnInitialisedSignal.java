package sbs.immovablerod.metaDungeon.signals;

import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.game.MonsterSpawn;

public class MonsterSpawnInitialisedSignal extends BaseSignal {

    private final MonsterSpawn monsterSpawn;

    public MonsterSpawnInitialisedSignal(MonsterSpawn monsterSpawn) {
        super();
        this.monsterSpawn = monsterSpawn;
    }

    public MonsterSpawn getMonsterSpawn() {
        return monsterSpawn;
    }
}
