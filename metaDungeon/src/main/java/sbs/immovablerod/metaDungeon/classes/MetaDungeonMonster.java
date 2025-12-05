package sbs.immovablerod.metaDungeon.classes;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sbs.immovablerod.metaDungeon.enums.Constants;
import sbs.immovablerod.metaDungeon.enums.Symbols;
import sbs.immovablerod.metaDungeon.MetaDungeon;

import java.util.ArrayList;

import static sbs.immovablerod.metaDungeon.util.EntityUtil.getScaledInt;

public class MetaDungeonMonster extends MetaDungeonEntity {
    private final static MetaDungeon plugin = MetaDungeon.getInstance();
    private final Entity entity;
    private final ArrayList<Object> effects;

    public MetaDungeonMonster(JsonNode template, Location spawnLocation, int level) {
        super(getScaledInt(template, "baseHealth", "healthScaling", level),
                getScaledInt(template, "baseDamage", "damageScaling", level),
                getScaledInt(template, "baseMovementSpeed", "movementSpeedScaling", level),
                getScaledInt(template, "baseDefence", "defenceScaling", level), 0, 0);



        this.entity = plugin.world.spawnEntity(spawnLocation,
                EntityType.valueOf(template.path("displayType").asText("ZOMBIE")));

        this.effects = new ArrayList<>();

        setMovementSpeed(this.movementSpeed);
        NBT.modify(this.entity, nbt -> {
            nbt.setBoolean("PersistenceRequired", true);
            nbt.setBoolean("IsBaby", template.at("/nbt/isBaby").asBoolean(false));
            nbt.setBoolean("CustomNameVisible", true);
            nbt.setBoolean("NoAI", template.at("/nbt/noAI").asBoolean(false));

            if (template.at("/nbt/powered").asBoolean(false)) {
                nbt.setBoolean("powered", true);
            }

            ReadWriteNBTCompoundList modifierCompound = nbt.getCompoundList("attributes");
            ReadWriteNBT movementCompound = modifierCompound.addCompound();
            movementCompound.setString("id", "minecraft:follow_range");
            movementCompound.setInteger("base", 20);

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
        //if (mainHand != null) livingEntity.getEquipment().setItemInMainHand(new ItemStack(Material.getMaterial(mainHand.toUpperCase())));
        //if (offHand != null) livingEntity.getEquipment().setItemInOffHand(new ItemStack(Material.getMaterial(offHand.toUpperCase())));
        //if (helmet != null) livingEntity.getEquipment().setHelmet(new ItemStack(Material.getMaterial(helmet.toUpperCase())));
        //if (chestplate != null) livingEntity.getEquipment().setChestplate(new ItemStack(Material.getMaterial(chestplate.toUpperCase())));
        //if (leggings != null) livingEntity.getEquipment().setLeggings(new ItemStack(Material.getMaterial(leggings.toUpperCase())));
        //if (boots != null) livingEntity.getEquipment().setBoots(new ItemStack(Material.getMaterial(boots.toUpperCase())));
        //livingEntity.setno;

        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 99999, 5, true, false));


        this.updateDisplay();
    }

    public void updateDisplay() {
//        NBT.modify(this.entity, nbt -> {
//            nbt.setString("CustomName", "{\"text\":\"@HEALTH\", \"color\":\"red\"}".replace(
//                    "@HEALTH", String.valueOf(this.health) + Symbols.HEART
//            ));
//        });
        NBT.modify(this.entity, nbt -> {
            nbt.mergeCompound(NBT.parseNBT("{CustomName:{\"text\":\"@HEALTH\",\"color\":\"red\"}}".replace(
                    "@HEALTH", String.valueOf(this.health) + Symbols.HEART)
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
            ReadWriteNBTCompoundList modifierCompound = nbt.getCompoundList("attributes");
            ReadWriteNBT movementCompound = modifierCompound.addCompound();
            movementCompound.setString("id", "minecraft:movement_speed");
            movementCompound.setDouble("base",  trueSpeed);
        });
    }

    public Entity getEntity() {
        return entity;
    }

    public void killed() {
        plugin.world.playEffect(this.entity.getLocation(), Effect.SMOKE, 40, 100);
        this.entity.remove();
    }

    @Override
    public void receiveAttack(MetaDungeonEntity attacker) {
        super.receiveAttack(attacker);
        ((LivingEntity) this.entity).setNoDamageTicks(0);
    }
}
