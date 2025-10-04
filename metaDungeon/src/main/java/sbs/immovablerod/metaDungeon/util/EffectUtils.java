package sbs.immovablerod.metaDungeon.util;

import sbs.immovablerod.metaDungeon.Enums.Effects;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.elements.BaseEffect;
import sbs.immovablerod.metaDungeon.elements.effects.HealthBoostAdd;
import sbs.immovablerod.metaDungeon.elements.effects.Speed;

public class EffectUtils {

    public static BaseEffect getEffect(String name, MetaDungeonEntity owner, int value, int duration) {
        if (name.equals(Effects.HEALTH_BOOST_ADD.toString())) return new HealthBoostAdd(owner, value, duration);
        if (name.equals(Effects.SPEED_BOOST.toString())) return new Speed(owner, value, duration);
        if (name.equals(Effects.DAMAGE_BOOST.toString())) return new Speed(owner, value, duration);
        else return null;
    }
}
