package sbs.immovablerod.metaDungeon.elements.items;

import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;

public class SmallHealthCrystal extends ItemInterface {
    private final MetaDungeonItem root;
    public SmallHealthCrystal(MetaDungeonItem root) {
        this.root = root;
    }

    @Override
    public void onRightClick(MetaDungeonPlayer player) {
        player.setMaxHealth(player.getMaxHealth() + this.root.getHealth());
        this.root.consume(player);
    }
}
