package sbs.immovablerod.metaDungeon.elements.items;

import sbs.immovablerod.metaDungeon.classes.MetaDungeonEntity;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.util.Random;

public class SoulEater extends ItemInterface {
    private final MetaDungeonItem root;
    int kills;
    public SoulEater(MetaDungeonItem root) {
        this.root = root;
        this.kills = 0;
    }

    @Override
    public void onCommitAttack(MetaDungeonEntity attacker, MetaDungeonEntity victim) {
        super.onCommitAttack(attacker, victim);
        if (attacker instanceof MetaDungeonPlayer) {

            if (GConfig.entityManager.isDead(victim.getId())) {
                ((MetaDungeonPlayer) attacker).changeMaxHealth(1);
                attacker.changeHealth(1);
                this.kills++;
            }
            if (Random.getRandInt(1, 100 - this.kills) == 1) {
                GConfig.messageManager.message(((MetaDungeonPlayer) attacker).getPlayer(), "Soul Eater has burst apart!", Colors.DARK_RED);
                attacker.changeHealth(-Random.getRandInt(1, this.kills));
                ((MetaDungeonPlayer) attacker).getPlayer().getInventory().remove(
                        ((MetaDungeonPlayer) attacker).getPlayer().getInventory().getItemInMainHand());
            }
        }
    }

}
