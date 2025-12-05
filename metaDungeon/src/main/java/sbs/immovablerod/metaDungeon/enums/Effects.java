package sbs.immovablerod.metaDungeon.enums;

import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.elements.EffectInterface;
import sbs.immovablerod.metaDungeon.elements.effects.Dummy;
import sbs.immovablerod.metaDungeon.elements.effects.Guarded;
import sbs.immovablerod.metaDungeon.elements.effects.Speed;
import sbs.immovablerod.metaDungeon.elements.effects.Strength;

import java.util.Objects;

public enum Effects
{
    DUMMY("dummy") {
        @Override
        public EffectInterface controller(MetaDungeonEffect root) {
            return new Dummy(root);
        }
    },
    STRENGTH("strength") {
        @Override
        public EffectInterface controller(MetaDungeonEffect root) {
            return new Strength(root);
        }
    },
    SPEED("speed") {
        @Override
        public EffectInterface controller(MetaDungeonEffect root) {
            return new Speed(root);
        }
    },
    GUARDED("guarded") {
        @Override
        public EffectInterface controller(MetaDungeonEffect root) {
            return new Guarded(root);
        }
    };


    final private String value;
    Effects(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public abstract EffectInterface controller(MetaDungeonEffect root);

    public static EffectInterface get(String abbr, MetaDungeonEffect root){
        for(Effects v : values()){
            if(Objects.equals(v.value, abbr)){
                return v.controller(root);
            }
        }
        return Effects.DUMMY.controller(root);
    }
}

