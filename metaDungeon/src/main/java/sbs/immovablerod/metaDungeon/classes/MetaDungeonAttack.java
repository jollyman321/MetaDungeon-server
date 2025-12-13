package sbs.immovablerod.metaDungeon.classes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.managers.PlayerManager;

public class MetaDungeonAttack {
    private static final Logger log = LogManager.getLogger(MetaDungeonAttack.class);
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    public final MetaDungeonEntity attacker;
    public final MetaDungeonEntity victim;
    private int damage;
    private boolean canceled;

    public MetaDungeonAttack(MetaDungeonEntity attacker, MetaDungeonEntity victim) {
        this.attacker = attacker;
        this.victim = victim;
        try {
            this.damage = attacker.getDamage() - Math.max(this.victim.getDefence() - this.attacker.getArmorPierce(), 0);
        } catch (NullPointerException e) {
            System.out.println(attacker);
            System.out.println(victim.getName());
        }
        this.canceled = false;
    }
    public void cancel() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return this.canceled;
    }
    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void resolve() {

        try {
            this.attacker.getEquipment().values().forEach(item -> {
                    item.getController().onCommitAttack(this.attacker, this.victim);
            });

            if (this.victim instanceof MetaDungeonPlayer) {
                for (MetaDungeonPlayer player : GConfig.playerManager.getAll()) {
                    System.out.println(player);
                    player.getEquipment().values().forEach(item -> {
                        item.getController().onTeamMemberReceiveAttack(player, this);
                    });
                }
            }
            if (!this.canceled) {
                this.attacker.onDealtAttack(this.victim);
                this.victim.receiveAttack(this.attacker);

                this.victim.setHealth(
                        this.victim.getHealth() - Math.max(this.damage, 1)
                );
                // knockback
                this.victim.setVelocity(this.attacker.getKnockback());
            }

        } catch (NullPointerException e) {
            if (this.attacker == null) {
                plugin.getLogger().warning("failed to execute attack (attacker is null)");

            } else {
                plugin.getLogger().warning("failed to execute attack " + this.attacker.getName() + " -> " + this.victim.getName());
                log.error("e: ", e);
            }
        }
    }
}
