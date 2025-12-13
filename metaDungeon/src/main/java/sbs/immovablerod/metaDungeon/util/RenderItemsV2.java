package sbs.immovablerod.metaDungeon.util;


import com.fasterxml.jackson.databind.JsonNode;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import sbs.immovablerod.metaDungeon.enums.Rarities;
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


           int defence = item.at("/defence").asInt(0);
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

            final TextComponent mcDisplayName = Component.text()
                    .content(displayName)
                    .color(Tiers.get(tier).color())
                    .decoration(TextDecoration.ITALIC, false)
                    .build();

           List<String> lore = new ArrayList<>();

           // ** Attributes **
           if (maxHealth > 0) lore.add(renderLoreLine("Max Health: " + maxHealth, NamedTextColor.GOLD));
           if (maxStamina > 0) lore.add(renderLoreLine("Max Stamina: " + maxStamina, NamedTextColor.GOLD));

           if (damage != 0) lore.add(renderLoreLine("Damage: " + damage, NamedTextColor.DARK_AQUA));
           if (damagePercent != 0) lore.add(renderLoreLine("Damage: +" + damagePercent + "%", NamedTextColor.DARK_AQUA));
           if (attackSpeed != 0) lore.add(renderLoreLine("Attack Speed: " + attackSpeed, NamedTextColor.DARK_AQUA));
           if (range != 0) lore.add(renderLoreLine("Range: " + range, NamedTextColor.DARK_AQUA));
           if (knockback != 0) lore.add(renderLoreLine("Knockback: " + knockback, NamedTextColor.DARK_AQUA));
           if (defence != 0) lore.add(renderLoreLine("Defence: " + defence, NamedTextColor.DARK_AQUA));
           if (movement != 0) lore.add(renderLoreLine("Movement Speed: " + movement, NamedTextColor.DARK_AQUA));
           if (staminaCost != 0) lore.add(renderLoreLine("Stamina Cost: " + staminaCost, NamedTextColor.DARK_AQUA));
           if (durability > -1) {
               nbt.getOrCreateCompound("custom_data").setInteger("durability_line", lore.size());
               lore.add(renderLoreLine("Durability: " + durability, NamedTextColor.DARK_AQUA));
           }


           if (item.at("/onConsume/changeHealth").asInt(0) != 0) {
               lore.add(renderLoreLine("Heals: " + item.at("/onConsume/changeHealth").asInt(), NamedTextColor.RED));
           }
           if (item.at("/onConsume/changeStamina").asInt(0) != 0) {
               lore.add(renderLoreLine("Stamina: " + item.at("/onConsume/changeStamina").asInt(), NamedTextColor.GREEN));
           }
           if (item.at("/onConsume/changeMaxHealth").asInt(0) != 0) {
               lore.add(renderLoreLine("Max Health: " + item.at("/onConsume/changeMaxHealth").asInt(), NamedTextColor.DARK_RED));
           }
           if (item.at("/onConsume/changeLives").asInt(0) != 0) {
               lore.add(renderLoreLine("Lives: " + item.at("/onConsume/changeLives").asInt(), NamedTextColor.GOLD));
           }
            if (!item.path("lore").asText().isEmpty()) {
                for (String line: item.path("lore").asText().split("\n")) {
                    lore.add(renderLoreLine(line, NamedTextColor.DARK_PURPLE));
                }
            }

           if (damage > 0 && attackSpeed > 0) {
               System.out.println("DPS for " + displayName + " = " + (float) damage * ( (float) attackSpeed/10));
           }
            // ** RARITY / CATEGORY **
            lore.add("{\"text\":\"\"}");
            lore.add(renderLoreLine(Rarities.get(rarity).toString() + " " + category,
                    Rarities.get(rarity).color()));

            ReadWriteNBT displayCompound = nbt.getOrCreateCompound("tooltip_display");
            displayCompound.getStringList("hidden_components").add("attribute_modifiers");


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



            nbt.setString("item_name", JSONComponentSerializer.json().serialize(mcDisplayName));
            for (String ele : lore) nbt.getStringList("lore").add(ele);

            //System.out.println(Serialize.serializeItem(item));
            output.put(item.path("internalName").asText(), nbt);

        }
        System.out.println("Rendered item default nbt for: " + output.keySet().toString());
        return output;


    }
    private static String renderLoreLine(String text, NamedTextColor color) {
        final TextComponent textComponent = Component.text()
                .content(text)
                .color(color)
                .decoration(TextDecoration.ITALIC, false)
                .build();

        return JSONComponentSerializer.json().serialize(textComponent);
    }
}
