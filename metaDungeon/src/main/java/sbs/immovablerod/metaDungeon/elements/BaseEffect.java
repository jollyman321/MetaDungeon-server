package sbs.immovablerod.metaDungeon.elements;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;

public class BaseEffect {
    public final MetaDungeon plugin = MetaDungeon.getInstance();
    public final MetaDungeonEntity owner;
    public final int duration;
    public final int value;

    public BaseEffect(MetaDungeonEntity owner, int value, int duration) {
        this.owner = owner;
        this.value = value;
        this.duration = duration;
    }
    public void onHitByAttack(MetaDungeonMonster x) {

    }

    public boolean isPersistent() {return false;}
}
