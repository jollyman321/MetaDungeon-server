package sbs.immovablerod.metaDungeon.enums;

import sbs.immovablerod.metaDungeon.classes.MetaDungeonEvent;

import sbs.immovablerod.metaDungeon.elements.EventInterface;
import sbs.immovablerod.metaDungeon.elements.events.AcidRain;
import sbs.immovablerod.metaDungeon.elements.events.Dummy;
import sbs.immovablerod.metaDungeon.elements.events.MonsterCore;
import sbs.immovablerod.metaDungeon.elements.events.MonsterRush;

import java.util.Objects;

public enum Events
{
    DUMMY("dummy") {
        @Override
        public EventInterface controller(MetaDungeonEvent root) {
            return new Dummy(root);
        }
    },
    ACID_RAIN("acidRain") {
        @Override
        public EventInterface controller(MetaDungeonEvent root) {
            return new AcidRain(root);
        }
    },
    MONSTER_RUSH("monsterRush") {
        @Override
        public EventInterface controller(MetaDungeonEvent root) {
            return new MonsterRush(root);
        }
    },
    MONSTER_CORE("monsterCore") {
        @Override
        public EventInterface controller(MetaDungeonEvent root) {
            return new MonsterCore(root);
        }
    };

    final private String value;
    Events(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public abstract EventInterface controller(MetaDungeonEvent root);

    public static EventInterface get(String abbr, MetaDungeonEvent root){
        for(Events v : values()){
            if(Objects.equals(v.value, abbr)){
                return v.controller(root);
            }
        }
        return Events.DUMMY.controller(root);
    }
}
