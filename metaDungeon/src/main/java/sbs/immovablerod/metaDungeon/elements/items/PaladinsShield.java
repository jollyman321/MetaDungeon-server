package sbs.immovablerod.metaDungeon.elements.items;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.classes.*;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.enums.Effects;

import java.util.ArrayList;
import java.util.Objects;

public class PaladinsShield extends ItemInterface {
    private final MetaDungeonItem root;
    private final ArrayList<MetaDungeonEffect> effects;


    public PaladinsShield(MetaDungeonItem root) {
        this.root = root;
        this.effects = new ArrayList<MetaDungeonEffect>();
    }

    @Override
    public void onCreated() {
        super.onCreated();

    }

    @Override
    public void onTeamMemberReceiveAttack(MetaDungeonEntity itemUser, MetaDungeonAttack attack) {
        super.onTeamMemberReceiveAttack(itemUser, attack);
        System.out.println("check1");
        if (itemUser instanceof MetaDungeonPlayer && attack.victim instanceof MetaDungeonPlayer) {
            System.out.println("check2");
            if (attack.victim != itemUser) {
                System.out.println("check3");
                MetaDungeonPlayer defender = (MetaDungeonPlayer) itemUser;
                MetaDungeonPlayer protect = (MetaDungeonPlayer) attack.victim;

                if (defender.getPlayer().getLocation().distance(protect.getPlayer().getLocation()) <= 10 &&
                        defender.getHealth() > protect.getHealth() &&
                        !defender.isDead()) {
                    System.out.println("check4");
                    attack.setDamage(attack.getDamage() / 2);
                    defender.changeHealth(attack.getDamage() / 2 - defender.getDefence());

                }

            }
        }
    }
}
