package sbs.immovablerod.metaDungeon.enums;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.classes.events.MetaDungeonEvent;

import sbs.immovablerod.metaDungeon.classes.events.*;

import java.util.Objects;

public enum Event
{
    DUMMY("dummy") {
        @Override
        public MetaDungeonEvent controller(String name, JsonNode template, int level) {
            return new Dummy(name, template, level);
        }
    },
    ACID_RAIN("acidRain") {
        @Override
        public MetaDungeonEvent controller(String name, JsonNode template, int level) {
            return new AcidRain(name, template, level);
        }
    },
    MONSTER_RUSH("monsterRush") {
        @Override
        public MetaDungeonEvent controller(String name, JsonNode template, int level) {
            return new MonsterRush(name, template, level);
        }
    },
    MONSTER_CORE("monsterCore") {
        @Override
        public MetaDungeonEvent controller(String name, JsonNode template, int level) {
            return new MonsterCore(name, template, level);
        }
    },
    MONSTER_INVASION("monsterInvasion") {
        @Override
        public MetaDungeonEvent controller(String name, JsonNode template, int level) {
            return new MonsterInvasion(name, template, level);
        }
    };

    final private String value;
    Event(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public abstract MetaDungeonEvent controller(String name, JsonNode template, int level);

    public static MetaDungeonEvent get(String name, JsonNode template, int level){
        for(Event v : values()){
            if(Objects.equals(v.value, name)){
                return v.controller(name, template, level);
            }
        }
        return Event.DUMMY.controller(name, template, level);
    }
}
