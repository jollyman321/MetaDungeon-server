package sbs.immovablerod.metaDungeon.classes;

import sbs.immovablerod.metaDungeon.elements.BaseEffect;
import sbs.immovablerod.metaDungeon.util.EffectUtils;

import java.util.ArrayList;

public class MetaDungeonEntity {
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

    public ArrayList<BaseEffect> activeEffects;

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

    public void addEffect(String effectName, int value, int duration) {

        BaseEffect effect = EffectUtils.getEffect(effectName, this, value, duration);
        if (effect != null) {
            if (effect.isPersistent()) {
                this.activeEffects.add(effect);
            }
        } else {
            System.out.println("could not find effect: " + effectName);
        }

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
