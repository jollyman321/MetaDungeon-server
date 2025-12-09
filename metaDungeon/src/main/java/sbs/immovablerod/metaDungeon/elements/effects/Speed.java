package sbs.immovablerod.metaDungeon.elements.effects;

import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.elements.EffectInterface;

public class Speed extends EffectInterface {
    private final MetaDungeonEffect root;
    public Speed(MetaDungeonEffect root) {
        this.root = root;
    }

    @Override
    public void onInitiated(MetaDungeonEntity entity) {
        super.onInitiated(entity);
        entity.changeMovementSpeed(this.root.getLevel());
    }

    @Override
    public void onClear(MetaDungeonEntity entity) {
        super.onClear(entity);
        entity.changeMovementSpeed(-this.root.getLevel());
    }
}