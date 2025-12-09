package sbs.immovablerod.metaDungeon.classes;

import com.fasterxml.jackson.databind.JsonNode;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.enums.Items;
import sbs.immovablerod.metaDungeon.game.GConfig;

import java.util.Objects;
import java.util.UUID;

import static java.lang.Math.round;
import static sbs.immovablerod.metaDungeon.game.GConfig.soundManager;

public class MetaDungeonItem extends ItemStack {
    public final String name;
    private final UUID id;
    public final JsonNode template;
    public final String category;
    public final Integer tier;
    public final Integer rarity;
    //public final Integer randomGen;
    public final Integer damage;
    public final Integer defence;
    public final Integer movement;
    public final Integer health;
    public final Integer stamina;

    private final Integer attack_speed;
    private final Integer staminaCost;
    private final Integer knockback;
    private final Integer damagePercent;
    private final Integer armorPierce;
    private final Double range;
    private final ItemInterface controller;
    public Integer durability;
    public Integer currentDurability;

    public MetaDungeonItem(
                         String name,
                         UUID id,
                         ReadWriteNBT baseNbt,
                         JsonNode template
    ) {
        super(Objects.requireNonNull(
                Material.getMaterial(template.path("displayType").asText("BARRIER").toUpperCase()
                )
        ));

        //this.item = item;
        this.name = name;
        this.id = id;
        this.template = template;
        this.category = template.at("/category").asText();
        this.tier = template.at("/tier").asInt();
        this.rarity = template.at("/rarity").asInt();
        this.damage = template.at("/damage").asInt();
        this.defence = template.at("/defence").asInt();
        this.movement = template.at("/movementSpeed").asInt();
        this.health = template.at("/health").asInt();
        this.stamina = template.at("/stamina").asInt();
        this.durability =template.at("/durability").asInt();
        this.range =template.at("/range").asDouble();

        this.attack_speed = template.at("/attackSpeed").asInt();
        this.armorPierce = template.at("/armorPierce").asInt();
        this.staminaCost = template.at("/staminaCost").asInt();

        this.knockback = template.at("/knockback").asInt();
        this.damagePercent = template.at("/damagePercent").asInt();
        
        this.currentDurability = this.durability;

        NBT.modifyComponents(this, nbt -> {
            nbt.mergeCompound(NBT.parseNBT(baseNbt.toString().replace("'", "")));
            nbt.getOrCreateCompound("custom_data").setUUID("id", id);
            //if (this.durability != null && this.durability != -1) {
            //    nbt.setInteger("Damage", this.maxInternalDurability - this.durability);
            //}
        });
        
        // implement custom item functions
        this.controller = Items.get(template.path("controller").asText(), this);
        this.controller.onCreated();
    }
    public void consume(MetaDungeonPlayer player) {

        player.changeMaxHealth(this.template.at("/onConsume/changeMaxHealth").asInt(0));
        player.changeHealth(this.template.at("/onConsume/changeHealth").asInt(0));
        player.changeStamina(this.template.at("/onConsume/changeStamina").asDouble(0));
        player.setLives(player.getLives() + this.template.at("/onConsume/changeLives").asInt(0));

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
    }

    public void changeDurability(int amount, ItemStack item, MetaDungeonPlayer player) {

        if (this.durability > -1) {
            this.currentDurability -= amount;
            NBT.modifyComponents(item, nbt -> {
                int location = nbt.getOrCreateCompound("minecraft:custom_data").getInteger("durability_line");
                nbt.getCompoundList("minecraft:lore").get(location).setString("text", "Durability: @1/@2".replace
                        ("@1", String.valueOf(this.currentDurability)).replace
                        ("@2", String.valueOf(this.durability))
                );
            });

            //Damageable meta = (Damageable) item.getItemMeta();
            //meta.setDamage(10);
            //meta.setMaxDamage(100);
            //item.setItemMeta(meta);
            item.setDurability((short) ((this.durability - this.currentDurability) * 2));

            if (this.currentDurability < 1) {
                // armor requires extra handling code
                int i = 0;
                for (ItemStack piece: player.getPlayer().getInventory().getArmorContents()) {
                    if (Objects.equals(GConfig.itemManager.getItemId(piece), this.getId())) {
                        if (i == 0) {
                            player.getPlayer().getInventory().setBoots(null);
                        } else if (i == 1) {
                            player.getPlayer().getInventory().setLeggings(null);
                        } else if (i == 2) {
                            player.getPlayer().getInventory().setChestplate(null);
                        } else if (i == 3) {
                            player.getPlayer().getInventory().setHelmet(null);
                        }
                        player.updateInventory();
                        soundManager.play(player.getPlayer().getLocation(), Sound.ITEM_SHIELD_BREAK);
                        break;
                    }
                    i++;
                }
            }

        }
    }

    public JsonNode getTemplate() {
        return template;
    }

    public UUID getId() {return this.id;}
    public Integer getAttackSpeed() {return this.attack_speed;}
    public Integer getStaminaCost() {return this.staminaCost;}
    public Integer getKnockback() {
        return this.knockback;
    }
    public Integer getDamage() {return this.damage;}
    public Integer getArmorPierce() {return this.armorPierce;}
    public Integer getDamagePercent() {return this.damagePercent;}
    public Integer getHealth() {return this.health;}
    public Integer getStamina() {return this.stamina;}
    public double getRange() {
        return this.range;
    }

    public Integer getDefence() {
        return this.defence;
    }

    public boolean isConsumable() {
        if (this.template.has("onConsume")) {
            return true;
        }
        return false;

    }
    public ItemInterface getController() {return this.controller;}
}
