package sbs.immovablerod.metaDungeon.classes;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.enums.Constants;
import sbs.immovablerod.metaDungeon.enums.Symbols;
import sbs.immovablerod.metaDungeon.MetaDungeon;

import java.util.ArrayList;

public class MetaDungeonMonster extends MetaDungeonEntity {
    private final static MetaDungeon plugin = MetaDungeon.getInstance();
    private final Entity entity;
    private ArrayList<Object> effects;

    public MetaDungeonMonster(EntityType type,
                              Location spawnLocation,
                              int health,
                              int damage,
                              int movement,
                              int defence,

                              String mainHand,
                              String offHand,
                              String helmet,
                              String chestplate,
                              String leggings,
                              String boots
                          ) {
        super(health, damage, movement, defence, 0, 0);
        this.entity = plugin.world.spawnEntity(spawnLocation, type);


        this.effects = new ArrayList<>();

        setMovementSpeed(this.movementSpeed);

        NBT.modify(this.entity, nbt -> {
            nbt.setBoolean("PersistenceRequired", true);
            nbt.setBoolean("Paper.ShouldBurnInDay", false);
            nbt.setBoolean("IsBaby", false);
            nbt.setBoolean("CustomNameVisible", true);

            ReadWriteNBTCompoundList modifierCompound = nbt.getCompoundList("Attributes");
            ReadWriteNBT movementCompound = modifierCompound.addCompound();
            movementCompound.setString("Name", "generic.follow_range");
            movementCompound.setInteger("Base", 20);

            // this doesn't work for some reason?

            /*if (mainHand != null) {
                ReadWriteNBTCompoundList handItemsCompound = nbt.getCompoundList("HandItems");
                ReadWriteNBT compound = handItemsCompound.addCompound();
                compound.setString("id", "minecraft:wooden_sword");
                compound.setByte("Count",  (byte) 1);
                ReadWriteNBT compound2 = handItemsCompound.addCompound();
            }*/
        });

        LivingEntity livingEntity = (LivingEntity) this.entity;
        if (mainHand != null) livingEntity.getEquipment().setItemInMainHand(new ItemStack(Material.getMaterial(mainHand.toUpperCase())));
        if (offHand != null) livingEntity.getEquipment().setItemInOffHand(new ItemStack(Material.getMaterial(offHand.toUpperCase())));
        if (helmet != null) livingEntity.getEquipment().setHelmet(new ItemStack(Material.getMaterial(helmet.toUpperCase())));
        if (chestplate != null) livingEntity.getEquipment().setChestplate(new ItemStack(Material.getMaterial(chestplate.toUpperCase())));
        if (leggings != null) livingEntity.getEquipment().setLeggings(new ItemStack(Material.getMaterial(leggings.toUpperCase())));
        if (boots != null) livingEntity.getEquipment().setBoots(new ItemStack(Material.getMaterial(boots.toUpperCase())));
        livingEntity.setNoDamageTicks(0);


        this.updateDisplay();
    }

    public void updateDisplay() {
        NBT.modify(this.entity, nbt -> {
            nbt.setString("CustomName", "{\"text\":\"@HEALTH\",\"color\":\"red\"}".replace(
                    "@HEALTH", String.valueOf(this.health) + Symbols.HEART
            ));
        });
    }

    @Override
    public void setHealth(int health) {
        super.setHealth(health);
        if (health <= 0) {
            this.killed();
        }
        this.updateDisplay();
    }

    @Override
    public void setMovementSpeed(float value) {
        this.movementSpeed = value;
        double trueSpeed = (double) movementSpeed / Constants.MONSTER_SPEED_MODIFIER.value();
        NBT.modify(this.entity, nbt -> {
            ReadWriteNBTCompoundList modifierCompound = nbt.getCompoundList("Attributes");
            ReadWriteNBT movementCompound = modifierCompound.addCompound();
            movementCompound.setString("Name", "generic.movement_speed");
            movementCompound.setDouble("Base",  trueSpeed);
        });
    }

    public Entity getEntity() {
        return entity;
    }

    public void killed() {
        plugin.world.playEffect(this.entity.getLocation(), Effect.SMOKE, 40);
        this.entity.remove();
    }
}
