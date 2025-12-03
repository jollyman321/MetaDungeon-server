package sbs.immovablerod.metaDungeon.classes;

import com.fasterxml.jackson.databind.JsonNode;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.enums.Items;

import java.util.UUID;

import static java.lang.Math.round;

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
    private final ItemInterface itemInterface;
    private final Integer armorPierce;
    public Integer durability;
    public Integer currentDurability;

    public MetaDungeonItem(
                         String name,
                         UUID id,
                         ReadWriteNBT baseNbt,
                         JsonNode template
    ) {
        super(Material.getMaterial( template.path("displayType").asText("BARRIER").toUpperCase()));

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
        this.itemInterface = Items.get(this.name, this);

    }
    public void consume(MetaDungeonPlayer player) {

        player.changeMaxHealth(this.template.at("/onConsume/changeMaxHealth").asInt(0));
        player.changeHealth(this.template.at("/onConsume/changeHealth").asInt(0));
        player.changeStamina(this.template.at("/onConsume/changeStamina").asDouble(0));

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

    public void changeDurability(int amount, ItemStack craftItemStack, MetaDungeonPlayer player) {

        if (this.durability > -1) {
            this.currentDurability -= amount;
            player.updateInventory();
            craftItemStack.setDurability((short) ((this.durability - this.currentDurability) * 2));
            NBT.modifyComponents(craftItemStack, nbt -> {
                int location = nbt.getOrCreateCompound("minecraft:custom_data").getInteger("durability_line");
                nbt.getCompoundList("minecraft:lore").get(location).setString("text", "Durability: @1/@2".replace
                        ("@1", String.valueOf(this.currentDurability)).replace
                        ("@2", String.valueOf(this.durability))
                );
            });
            if (this.durability < 1) {
                for (int i = 0; i < player.getPlayer().getInventory().getSize(); i++) {
                    if (player.getPlayer().getInventory().getItem(i).getDurability() < 1)
                        player.getPlayer().getInventory().getItem(i).setType(Material.AIR);
                }
            }
        }
    }


    public UUID getId() {return this.id;}
    public Integer getAttackSpeed() {return this.attack_speed;}
    public Integer getStaminaCost() {return this.staminaCost;}
    public Integer getKnockback() {return this.knockback;}
    public Integer getDamage() {return this.damage;}
    public Integer getArmorPierce() {return this.armorPierce;}
    public Integer getDamagePercent() {return this.damagePercent;}
    public Integer getHealth() {return this.health;}
    public Integer getStamina() {return this.stamina;}

    public boolean isConsumable() {
        if (this.template.has("onConsume")) {
            return true;
        }
        return false;

    }
    public ItemInterface getInterface() {return this.itemInterface;}
}
