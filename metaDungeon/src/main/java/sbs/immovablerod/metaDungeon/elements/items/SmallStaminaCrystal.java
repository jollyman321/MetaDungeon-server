package sbs.immovablerod.metaDungeon.elements.items;

import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;

public class SmallStaminaCrystal extends ItemInterface {
    private final MetaDungeonItem root;
    public SmallStaminaCrystal(MetaDungeonItem root) {
        this.root = root;
    }

    @Override
    public void onRightClick(MetaDungeonPlayer player) {
        System.out.println("check");
        player.setMaxStamina(player.getMaxStamina() + this.root.getStamina());
        this.root.consume(player);
    }
}
