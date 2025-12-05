package sbs.immovablerod.metaDungeon.util;


import com.fasterxml.jackson.databind.JsonNode;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import org.apache.commons.text.WordUtils;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.enums.Rarities;
import sbs.immovablerod.metaDungeon.enums.Symbols;
import sbs.immovablerod.metaDungeon.enums.Tiers;

import java.util.*;

public class RenderItemsV2 {

    public static HashMap<String, ReadWriteNBT> renderBaseItemNbt(JsonNode items) {

        HashMap<String, ReadWriteNBT> output = new HashMap<>();
        Iterator<JsonNode> fields = items.elements();

        while (fields.hasNext()) {
           JsonNode item = fields.next();

           // load item parameters
           String displayName = item.at("/displayName").asText("null");
           String category = item.at("/category").asText("NOCATEGORY");
           int damage = item.at("/damage").asInt(0);
           float range = (float) item.at("/range").asDouble(0);


           int defence = item.at("/damage").asInt(0);
           int movement = item.at("/movementSpeed").asInt(0);
           int attackSpeed = item.at("/attackSpeed").asInt(0);
           int staminaCost = item.at("/staminaCost").asInt(0);
           int knockback = item.at("/knockback").asInt(0);
           int damagePercent = item.at("/damagePercent").asInt(0);

           int maxHealth = item.at("/maxHealth").asInt(0);
           int maxStamina = item.at("/maxStamina").asInt(0);

           int durability = item.at("/durability").asInt(-1);

           int rarity = item.at("/rarity").asInt(1);
           int tier = item.at("/tier").asInt(1);

           // generate nbt data
           ReadWriteNBT nbt = NBT.createNBTObject();

           String mcDisplayName = "{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"@COLOR\",\"text\":\"@NAME\"}],\"text\":\"\"}".replace(
                   "@COLOR", Tiers.get(tier).color().toString()).replace("@NAME", displayName);

           List<String> lore = new ArrayList<>();

           // ** Attributes **
           if (maxHealth > 0) lore.add(renderLoreLine("Max Health: " + maxHealth, Colors.GOLD));
           if (maxStamina > 0) lore.add(renderLoreLine("Max Stamina: " + maxStamina, Colors.GOLD));

           if (damage > 0) lore.add(renderLoreLine("Damage: " + damage, Colors.DARK_AQUA));
           if (damagePercent != 0) lore.add(renderLoreLine("Damage: +" + damagePercent + "%", Colors.DARK_AQUA));
           if (attackSpeed > 0) lore.add(renderLoreLine("Attack Speed: " + attackSpeed, Colors.DARK_AQUA));
           if (range > 0) lore.add(renderLoreLine("Range: " + range, Colors.DARK_AQUA));
           if (knockback > 0) lore.add(renderLoreLine("Knockback: " + knockback, Colors.DARK_AQUA));
           if (defence > 0) lore.add(renderLoreLine("Defence: " + defence, Colors.DARK_AQUA));
           if (movement != 0) lore.add(renderLoreLine("Movement Speed: " + movement, Colors.DARK_AQUA));
           if (staminaCost > 0) lore.add(renderLoreLine("Stamina Cost: " + staminaCost, Colors.DARK_AQUA));
           if (durability > -1) {
               nbt.getOrCreateCompound("custom_data").setInteger("durability_line", lore.size());
               lore.add(renderLoreLine("Durability: " + durability, Colors.DARK_AQUA));
           }
           if (damage > 0 && attackSpeed > 0) {
               System.out.println("DPS for " + displayName + " = " + (float) damage * ( (float) attackSpeed/10));
           }
            // ** RARITY / CATEGORY **
            lore.add("{\"text\":\"\"}");
            lore.add(renderLoreLine(Rarities.get(rarity).toString() + " " + WordUtils.capitalize(category),
                    Rarities.get(rarity).color()));

            ReadWriteNBT displayCompound = nbt.getOrCreateCompound("tooltip_display");
            displayCompound.getStringList("hidden_components").add("attribute_modifiers");

            ReadWriteNBT attributeModifiersRange = nbt.getCompoundList("attribute_modifiers").addCompound();
            attributeModifiersRange.setString("id", "entity_interaction_range");
            attributeModifiersRange.setString("type", "entity_interaction_range");
            attributeModifiersRange.setString("operation", "add_value");
            attributeModifiersRange.setFloat("amount", range);

            // consumables
            if (item.has("consumable")) {
                nbt.getOrCreateCompound("consumable").setFloat("consume_seconds", (float) item.at("/consumable/useTime").asDouble(0));
                nbt.getOrCreateCompound("consumable").setString("animation", item.at("/consumable/animation").asText("eat"));
                nbt.getOrCreateCompound("consumable").setString("sound", item.at("/consumable/sound").asText("entity.generic.eat"));
                nbt.getOrCreateCompound("consumable").setBoolean("has_consume_particles", item.at("/consumable/particles").asBoolean(true));
            }

            if (durability > -1) {
                nbt.setInteger("max_damage", durability * 2);
                nbt.setInteger("max_stack_size", 1);
            }

            //nbt.setInteger("Damage", item.getType().getMaxDurability() - Integer.parseInt(table.get(itemName).get("durability")));



            nbt.setString("item_name", displayName);
            for (String ele : lore) nbt.getStringList("lore").add(ele);

            //System.out.println(Serialize.serializeItem(item));
            output.put(item.path("internalName").asText(), nbt);

        }
        System.out.println("Rendered item default nbt for: " + output.keySet().toString());
        return output;


    }
    private static String renderLoreLine(String text, Colors color) {
        String string = "{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"@COLOR\",\"text\":\"@TEXT\"}";
        // "{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"@COLOR\",\"text\":\"@TEXT\"}],\"text\":\"\"}";

        string = string.replace("@TEXT", text);
        string = string.replace("@COLOR", color.toString());
        return string;
    }
}
