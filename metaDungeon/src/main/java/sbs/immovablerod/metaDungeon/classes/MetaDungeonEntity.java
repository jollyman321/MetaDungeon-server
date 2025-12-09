package sbs.immovablerod.metaDungeon.classes;

import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.enums.Effects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    public List<MetaDungeonEffect> activeEffects;

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

    }

    public void onDealtAttack(MetaDungeonEntity victum) {

    }

    public void kill() {
        // placeholder for subclasses
    }

    public String getName() {
        return "BASE_ENTITY";
    }

    public UUID getId() {
        return null;
    }
    public int getDamage() {
        return this.damage;
    }
    public int getDefence() {
        return this.defence;
    }
    public int getArmorPierce() {
        return this.armorPierce;
    }
    public float getMovementSpeed() {
        return this.movementSpeed;
    }
    public @NotNull Vector getKnockback() {
        return null;
    }
    public int getHealth() {
        return this.health;
    }


    public HashMap<String, MetaDungeonItem> getEquipment() {
        // placeholder;  atm monsters can't have gear
        return new HashMap<>();
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setMovementSpeed(float value) {
        this.movementSpeed = value;
    }

    public void changeHealth(int amount) {
        this.setHealth(amount + this.health);
    }
    public void changeDamage(int amount) {
        this.setDamage(amount + this.damage);
    }
    public void changeDefence(int amount) {
        this.setDefence(amount + this.defence);
    }
    public void changeMovementSpeed(int amount) {
        this.setMovementSpeed(amount + this.movementSpeed);
    }
    public void setVelocity(@NotNull Vector knockback) {
        // placeholder should be overwritten in child classes
    }
}
