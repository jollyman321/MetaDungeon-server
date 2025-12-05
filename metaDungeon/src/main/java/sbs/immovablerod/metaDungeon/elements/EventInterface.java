package sbs.immovablerod.metaDungeon.elements;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;

public class EventInterface {
    protected final MetaDungeon plugin = MetaDungeon.getInstance();
    public void onInitiated() {};
    public void onCompleted() {};
}
