package sbs.immovablerod.metaDungeon.classes;

import java.util.UUID;

public class MetaDungeonProjectile {
    private UUID ownerId;
    private int damage;

    public MetaDungeonProjectile(UUID ownerId, int damage) {
        this.ownerId = ownerId;
        this.damage = damage;
    }

    public int getDamage() {
        return this.damage;
    }
}
