package sbs.immovablerod.metaDungeon.classes.events;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;
import sbs.immovablerod.metaDungeon.game.MonsterSpawn;
import sbs.immovablerod.metaDungeon.util.Random;

import java.util.ArrayList;
import java.util.UUID;

import static sbs.immovablerod.metaDungeon.game.GConfig.taskManager;

public class MonsterInvasion extends MetaDungeonEvent {
    private ArrayList<MetaDungeonMonster> monsters ;
    public MonsterInvasion(String name, JsonNode template, int level) {
        super(name, template, level);

        this.monsters = new ArrayList<>();
    }

    @Override
    public void onInitiated() {
        super.onInitiated();
       this.startUpdateLoop(20L * 15L);
    }

    @Override
    protected void onUpdate() {
        int monsterCount = Random.getRandInt(this.getTemplate().path("waveSpawnsMin").asInt(10),
                this.getTemplate().path("waveSpawnsMax").asInt(30));
        MonsterSpawn wave = new MonsterSpawn(monsterCount, this.getLevel(), this.getLevel() * 3);
        this.monsters.addAll(wave.getMonsters());
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        this.monsters.forEach(MetaDungeonMonster::kill);
    }
}
