package sbs.immovablerod.metaDungeon.classes;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.enums.Items;

import java.util.UUID;

import static java.lang.Math.round;

public class MetaDungeonItem extends ItemStack {
    public final String name;
    public final String category;
    public final Integer tier;
    public final Integer rarity;
    public final Integer cost;
    public final Integer consumable;
    public final Integer randomGen;
    public final Integer damage;
    public final Integer defence;
    public final Integer movement;
    public final Integer health;
    public final Integer healLife;
    public final Integer weight;
    public final Integer stamina;
    public final Integer healStamina;
    private final UUID id;
    private final Integer attack_speed;
    private final Integer staminaCost;
    private final Integer knockback;
    private final Integer damagePercent;
    private final ItemInterface itemInterface;
    private Integer maxInternalDurability;
    public Integer durability;
    public Integer currentDurability;

    public MetaDungeonItem(
                         String name,
                         UUID id,
                         ReadWriteNBT baseNbt,
                         String type,
                         String category,
                         Integer tier,
                         Integer rarity,
                         Integer cost,
                         Integer consumable,
                         Integer randomGen,
                         Integer damage,
                         Integer defence,
                         Integer movement,
                         Integer health,
                         Integer stamina,
                         Integer weight,
                         Integer healLife,
                         Integer healStamina,
                         Integer durability,
                         Integer attack_speed,
                         Integer staminaCost,
                         Integer knockback,
                         Integer damagePercent

    ) {
        super(Material.getMaterial(type.toUpperCase()));


        //this.item = item;
        this.name = name;
        this.id = id;
        this.category = category;
        this.tier = tier;
        this.rarity = rarity;
        this.cost = cost;
        this.consumable = consumable;

        this.randomGen = randomGen;
        this.damage = damage;
        this.defence = defence;
        this.movement = movement;
        this.health = health;
        this.stamina = stamina;
        this.weight = weight;
        this.healLife = healLife;
        this.healStamina = healStamina;
        this.durability = durability;

        this.attack_speed = attack_speed;
        this.staminaCost = staminaCost;

        this.knockback = knockback;
        this.damagePercent = damagePercent;
        
        this.currentDurability = this.durability;
        this.maxInternalDurability = (int) this.getType().getMaxDurability();

        NBT.modify(this, nbt -> {
            nbt.mergeCompound(baseNbt);
            nbt.setUUID("id", id);
            if (this.durability != null && this.durability != -1) {
                nbt.setInteger("Damage", this.maxInternalDurability - this.durability);
            }
        });
        
        // implement custom item functions
        this.itemInterface = Items.get(this.name, this);

    }
    public void consume(MetaDungeonPlayer player) {
        player.changeHealth(this.getHealLife());
        player.changeStamina((double) this.getHealStamina());

        if (player.getPlayer().getInventory().getItemInMainHand().getAmount() > 1) {
            player.getPlayer().getInventory().getItemInMainHand().setAmount(player.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
        } else {
            player.getPlayer().getInventory().setItemInMainHand(null);
        }
        player.getGear().put("mainHand", null);

        // remove item from plugin.items?
    }

    public void onTargetHit(MetaDungeonPlayer player) {
        player.changeStamina((double) - this.staminaCost);
    /*
        if (this.durability != null && this.durability != -1) {
            this.currentDurability -= 1;
            System.out.println(this.currentDurability);
            System.out.println(round(this.maxInternalDurability - ((float) this.currentDurability / (float)  this.durability) * (float)  this.maxInternalDurability));
            NBT.modify(this, nbt -> {
                nbt.setInteger("Damage", 10); //round(this.maxInternalDurability - ((float) this.currentDurability / (float)  this.durability) * (float)  this.maxInternalDurability));
            });
        }*/
    }
    public UUID getId() {return this.id;}
    public Integer getAttackSpeed() {return this.attack_speed;}
    public Integer getStaminaCost() {return this.staminaCost;}
    public Integer getKnockback() {return this.knockback;}
    public Integer getDamage() {return this.damage;}
    public Integer getDamagePercent() {return this.damagePercent;}
    public Integer getHealth() {return this.health;}
    public Integer getStamina() {return this.stamina;}

    public Integer getHealLife() {return this.healLife;}
    public Integer getHealStamina() {return this.healStamina;}

    public ItemInterface getInterface() {return this.itemInterface;}
}
