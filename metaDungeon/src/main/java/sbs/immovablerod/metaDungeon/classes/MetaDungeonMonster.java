package sbs.immovablerod.metaDungeon.classes;

import com.fasterxml.jackson.databind.JsonNode;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import sbs.immovablerod.metaDungeon.enums.Constants;
import sbs.immovablerod.metaDungeon.enums.Symbols;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.game.GConfig;

import java.util.ArrayList;
import java.util.UUID;

import static sbs.immovablerod.metaDungeon.util.EntityUtil.getScaledInt;

public class MetaDungeonMonster extends MetaDungeonEntity {
    private final static MetaDungeon plugin = MetaDungeon.getInstance();
    private final Entity entity;
    private final ArrayList<Object> effects;
    private final LivingEntity livingEntity;

    public MetaDungeonMonster(JsonNode template, Location spawnLocation, int level) {
        super(getScaledInt(template, "baseHealth", "healthScaling", level),
                getScaledInt(template, "baseDamage", "damageScaling", level),
                getScaledInt(template, "baseMovementSpeed", "movementSpeedScaling", level),
                getScaledInt(template, "baseDefence", "defenceScaling", level), 30, 0);

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

        this.livingEntity = (LivingEntity) this.entity;
        //if (mainHand != null) livingEntity.getEquipment().setItemInMainHand(new ItemStack(Material.getMaterial(mainHand.toUpperCase())));
        //if (offHand != null) livingEntity.getEquipment().setItemInOffHand(new ItemStack(Material.getMaterial(offHand.toUpperCase())));
        //if (helmet != null) livingEntity.getEquipment().setHelmet(new ItemStack(Material.getMaterial(helmet.toUpperCase())));
        //if (chestplate != null) livingEntity.getEquipment().setChestplate(new ItemStack(Material.getMaterial(chestplate.toUpperCase())));
        //if (leggings != null) livingEntity.getEquipment().setLeggings(new ItemStack(Material.getMaterial(leggings.toUpperCase())));
        //if (boots != null) livingEntity.getEquipment().setBoots(new ItemStack(Material.getMaterial(boots.toUpperCase())));
        //livingEntity.setno;

        this.livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 99999, 5, true, false));


        this.updateDisplay();
    }

    @Override
    public String getName() {
        return this.entity.getName();
    }

    @Override
    public UUID getId() {
        return this.entity.getUniqueId();
    }

    public void updateDisplay() {
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
            this.kill();
        }
        this.updateDisplay();
    }

    @Override
    public void setMovementSpeed(float value) {
        this.movementSpeed = value;
        double trueSpeed = (double) this.movementSpeed / Constants.MONSTER_SPEED_MODIFIER.value();
        try {
            this.livingEntity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(trueSpeed);
            System.out.println("check");
        } catch (NullPointerException e) {
            NBT.modify(this.entity, nbt -> {
                ReadWriteNBTCompoundList modifierCompound = nbt.getCompoundList("attributes");
                ReadWriteNBT movementCompound = modifierCompound.addCompound();
                movementCompound.setString("id", "minecraft:movement_speed");
                movementCompound.setDouble("base",  trueSpeed);
            });
        }

        System.out.println(this.livingEntity);
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public void kill() {
        plugin.world.playEffect(this.entity.getLocation(), Effect.SMOKE, 40, 100);
        GConfig.entityManager.kill(this.getEntity().getUniqueId());
        this.entity.remove();
    }

    @Override
    public void receiveAttack(MetaDungeonEntity attacker) {
        super.receiveAttack(attacker);
        ((LivingEntity) this.entity).setNoDamageTicks(0);
    }

    @Override
    public @NotNull Vector getKnockback() {
        return this.entity.getLocation().getDirection().setY(0).normalize().multiply(((float) this.knockback)/30);
    }

    @Override
    public void setVelocity(@NotNull Vector knockback) {
        this.entity.setVelocity(knockback);
    }
}
