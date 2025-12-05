package sbs.immovablerod.metaDungeon.elements.effects;

import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.elements.EffectInterface;

public class Strength extends EffectInterface {
    private final MetaDungeonEffect root;
    public Strength(MetaDungeonEffect root) {
        this.root = root;
    }

    @Override
    public void onUpdate(MetaDungeonEntity entity) {
        entity.damage += this.root.getLevel();
    }
}