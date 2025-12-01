package sbs.immovablerod.metaDungeon.util;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import org.apache.commons.text.WordUtils;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.enums.Colors;
import sbs.immovablerod.metaDungeon.enums.Rarities;
import sbs.immovablerod.metaDungeon.enums.Symbols;
import sbs.immovablerod.metaDungeon.enums.Tiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RenderItems {

    public static HashMap<String, ReadWriteNBT> renderBaseItemNbt(HashMap<String, HashMap<String, String>> table) {

        HashMap<String, ReadWriteNBT> items = new HashMap<>();

        for (String itemName : table.keySet()) {

            HashMap<String, String> itemData = table.get(itemName);
            int damage = Integer.parseInt(itemData.get("damage"));
            float range = Float.parseFloat(itemData.get("range"));
            int defence = Integer.parseInt(itemData.get("defence"));
            int movement = Integer.parseInt(itemData.get("movement"));
            int attackSpeed = Integer.parseInt(itemData.get("attackSpeed"));
            int staminaCost = Integer.parseInt(itemData.get("staminaCost"));
            int heal_life = Integer.parseInt(itemData.get("heal_life"));
            int heal_stamina = Integer.parseInt(itemData.get("heal_stamina"));
            int knockback = Integer.parseInt(itemData.get("knockback"));
            int damagePercent = Integer.parseInt(itemData.get("damagePercent"));

            int maxHealth = Integer.parseInt(itemData.get("maxHealth"));
            int maxStamina = Integer.parseInt(itemData.get("maxStamina"));

            int durability = Integer.parseInt(itemData.get("durability"));

            int rarity = Integer.parseInt(itemData.get("rarity"));
            int tier = Integer.parseInt(itemData.get("tier"));

            // edit nbt
            ReadWriteNBT nbt = NBT.createNBTObject();

            // ** NAME **
            String displayName = "{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"@COLOR\",\"text\":\"@NAME\"}],\"text\":\"\"}".replace(
                    "@COLOR", Tiers.get(tier).color().toString()).replace("@NAME", WordUtils.capitalize(itemName.replace("_", " ")));

            List<String> lore = new ArrayList<>();


            // ** Attributes **
            if (maxHealth > 0) lore.add(renderLoreLine("Max Health: " + maxHealth, Colors.GOLD));
            if (maxStamina > 0) lore.add(renderLoreLine("Max Stamina: " + maxStamina, Colors.GOLD));

            if (damage > 0) lore.add(renderLoreLine("Damage: " + damage, Colors.DARK_AQUA));
            if (damagePercent != 0) lore.add(renderLoreLine("Damage: +" + damagePercent + "%", Colors.DARK_AQUA));
            if (attackSpeed > 0) lore.add(renderLoreLine("Attack Speed: " + attackSpeed, Colors.DARK_AQUA));
            if (damage > 0) lore.add(renderLoreLine("Range: " + range, Colors.DARK_AQUA));
            if (knockback > 0) lore.add(renderLoreLine("Knockback: " + knockback, Colors.DARK_AQUA));
            if (defence > 0) lore.add(renderLoreLine("Defence: " + defence, Colors.DARK_AQUA));
            if (movement != 0) lore.add(renderLoreLine("Movement Speed: " + movement, Colors.DARK_AQUA));
            if (staminaCost > 0) lore.add(renderLoreLine("Stamina Cost: " + staminaCost, Colors.DARK_AQUA));
            if (durability > -1) {
                nbt.getOrCreateCompound("custom_data").setInteger("durability_line", lore.size());
                lore.add(renderLoreLine("Durability: " + durability, Colors.DARK_AQUA));
            }


            if (heal_life > 0) lore.add(renderLoreLine("Heals: " + heal_life, Symbols.HEAL_LIFE.color()));
            if (heal_stamina > 0) lore.add(renderLoreLine("Stamina: " + heal_stamina, Symbols.HEAL_STAMINA.color()));

            if (damage > 0 && attackSpeed > 0) {
                System.out.println("DPS for " + itemName + " = " + (float) damage * ( (float) attackSpeed/10));
            }

            // ** RARITY / CATEGORY **
            lore.add("{\"text\":\"\"}");
            lore.add(renderLoreLine(Rarities.get(rarity).toString() + " " + WordUtils.capitalize(itemData.get("category")),
                    Rarities.get(rarity).color()));

            ReadWriteNBT displayCompound = nbt.getOrCreateCompound("tooltip_display");
            displayCompound.getStringList("hidden_components").add("attribute_modifiers");

            ReadWriteNBT attributeModifiersRange = nbt.getCompoundList("attribute_modifiers").addCompound();
            attributeModifiersRange.setString("id", "entity_interaction_range");
            attributeModifiersRange.setString("type", "entity_interaction_range");
            attributeModifiersRange.setString("operation", "add_value");
            attributeModifiersRange.setFloat("amount", range);

            if (durability > -1) {
                nbt.setInteger("max_damage", durability * 2);
                nbt.setInteger("max_stack_size", 1);
            }

            //nbt.setInteger("Damage", item.getType().getMaxDurability() - Integer.parseInt(table.get(itemName).get("durability")));



            nbt.setString("item_name", displayName);

            for (String ele : lore) nbt.getStringList("lore").add(ele);

            //System.out.println(Serialize.serializeItem(item));
            items.put(itemName, nbt);

        }
        System.out.println("Rendered item default nbt for: " + items.keySet().toString());
        return items;


    }
    private static String renderLoreLine(String text, Colors color) {
        String output = "{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"@COLOR\",\"text\":\"@TEXT\"}";
        // "{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"@COLOR\",\"text\":\"@TEXT\"}],\"text\":\"\"}";

        output = output.replace("@TEXT", text);
        output = output.replace("@COLOR", color.toString());
        return output;
    }
}
