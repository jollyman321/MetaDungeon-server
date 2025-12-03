package sbs.immovablerod.metaDungeon.classes;

import org.bukkit.Bukkit;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.enums.Effects;
import java.util.ArrayList;

public class MetaDungeonEntity {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    public int baseHealth;
    public int baseDamage;
    public float baseMovementSpeed;
    public int baseDefence;
    public int baseKnockback;
    public int baseArmorPierce;

    public int health;
    public int damage;
    public float movementSpeed;
    public int defence;
    public int knockback;
    public int armorPierce;

    public ArrayList<MetaDungeonEffect> activeEffects;

    public MetaDungeonEntity (
            int health,
            int damage,
            float movement,
            int defence,
            int knockback,
            int armorPierce
    ) {
        this.baseHealth = health;
        this.baseDamage = damage;
        this.baseMovementSpeed = movement;
        this.baseDefence = defence;
        this.baseKnockback = knockback;
        this.baseArmorPierce = armorPierce;

        this.health = health;
        this.damage = damage;
        this.movementSpeed = movement;
        this.defence = defence;
        this.knockback = knockback;
        this.armorPierce = armorPierce;

        this.activeEffects = new ArrayList<>();
    }
    public void receiveAttack(MetaDungeonEntity attacker) {
        this.setHealth(this.health - Math.max(
                attacker.getDamage() - Math.max(this.defence - attacker.getArmorPierce(), 0),
                1));
    }

    public void addEffect(MetaDungeonEffect effect) {

        this.activeEffects.add(effect);

        plugin.tasks.add(Bukkit.getScheduler().runTaskLater(plugin, () -> {
            effect.getController().onClear(this);
            this.activeEffects.remove(effect);
        }, 20L * effect.getDuration()));

        this.updateStats();

    }

    public void updateStats() {
        this.defence = this.baseDefence;
        this.damage = this.baseDamage;
        this.movementSpeed = this.baseMovementSpeed;
        this.knockback = this.baseKnockback;
        this.armorPierce = this.baseArmorPierce;

        for (MetaDungeonEffect effect : this.activeEffects) {
            effect.getController().onUpdate(this);
        }

        this.setMovementSpeed(this.movementSpeed);
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public void setMovementSpeed(float value) {
        this.movementSpeed = value;
    }
    public int getHealth() {
        return this.health;
    }
    public int getDamage() {
        return this.damage;
    }
    public int getArmorPierce() {
        return this.armorPierce;
    }
    public float getMovementSpeed() {
        return this.movementSpeed;
    }
    public int getKnockback() {return knockback;}
}
