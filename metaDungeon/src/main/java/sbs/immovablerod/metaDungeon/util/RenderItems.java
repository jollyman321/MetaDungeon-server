package sbs.immovablerod.metaDungeon.util;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.apache.commons.lang.WordUtils;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.Enums.Colors;
import sbs.immovablerod.metaDungeon.Enums.Rarities;
import sbs.immovablerod.metaDungeon.Enums.Symbols;
import sbs.immovablerod.metaDungeon.Enums.Tiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RenderItems {

    public static HashMap<String, ReadWriteNBT> renderBaseItemNbt(HashMap<String, HashMap<String, String>> table) {

        HashMap<String, ReadWriteNBT> items = new HashMap<>();

        for (String itemName : table.keySet()) {

            HashMap<String, String> itemData = table.get(itemName);
            int damage = Integer.parseInt(itemData.get("damage"));
            int defence = Integer.parseInt(itemData.get("defence"));
            int movement = Integer.parseInt(itemData.get("movement"));
            int attackSpeed = Integer.parseInt(itemData.get("attackSpeed"));
            int staminaCost = Integer.parseInt(itemData.get("staminaCost"));
            int heal_life = Integer.parseInt(itemData.get("heal_life"));
            int heal_stamina = Integer.parseInt(itemData.get("heal_stamina"));
            int knockback = Integer.parseInt(itemData.get("knockback"));
            int damagePercent = Integer.parseInt(itemData.get("damagePercent"));

            int rarity = Integer.parseInt(itemData.get("rarity"));
            int tier = Integer.parseInt(itemData.get("tier"));

            // edit nbt
            ReadWriteNBT nbt = NBT.createNBTObject();
            ReadWriteNBT displayCompound = nbt.getOrCreateCompound("display");
            // ** NAME **
            String displayName = "{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"@COLOR\",\"text\":\"@NAME\"}],\"text\":\"\"}".replace(
                    "@COLOR", Tiers.get(tier).color().toString()).replace("@NAME", WordUtils.capitalize(itemName.replace("_", " ")));

            List<String> lore = new ArrayList<>();


            // ** Attributes **
            if (damage > 0) lore.add(renderLoreLine("Damage: " + damage, Colors.DARK_AQUA));
            if (damagePercent != 0) lore.add(renderLoreLine("Damage: +" + damagePercent + "%", Colors.DARK_AQUA));
            if (attackSpeed > 0) lore.add(renderLoreLine("Attack Speed: " + attackSpeed, Colors.DARK_AQUA));
            if (knockback > 0) lore.add(renderLoreLine("Knockback: " + knockback, Colors.DARK_AQUA));
            if (defence > 0) lore.add(renderLoreLine("Defence: " + defence, Colors.DARK_AQUA));
            if (movement != 0) lore.add(renderLoreLine("Movement Speed: " + movement, Colors.DARK_AQUA));
            if (staminaCost > 0) lore.add(renderLoreLine("Stamina Cost: " + staminaCost, Colors.DARK_AQUA));


            if (heal_life > 0) lore.add(renderLoreLine("Heals: " + heal_life, Symbols.HEAL_LIFE.color()));
            if (heal_stamina > 0) lore.add(renderLoreLine("Stamina: " + heal_stamina, Symbols.HEAL_STAMINA.color()));

            if (damage > 0 && attackSpeed > 0) {
                System.out.println("DPS for " + itemName + " = " + (float) damage * ( (float) attackSpeed/10));
            }

            // ** RARITY / CATEGORY **
            lore.add("{\"text\":\"\"}");
            lore.add(renderLoreLine(Rarities.get(rarity).toString() + " " + WordUtils.capitalize(itemData.get("category")),
                    Rarities.get(rarity).color()));

            nbt.setInteger("HideFlags", 127);
            //nbt.setInteger("Damage", item.getType().getMaxDurability() - Integer.parseInt(table.get(itemName).get("durability")));
            displayCompound.setString("Name", displayName);

            for (String ele : lore) displayCompound.getStringList("Lore").add(ele);

            //System.out.println(Serialize.serializeItem(item));
            items.put(itemName, nbt);

        }
        System.out.println("Rendered item default nbt for: " + items.keySet().toString());
        return items;


    }
    public static HashMap<String, String> getCustomNameMappings(HashMap<String, ItemStack> renderedItems) {
        HashMap<String, String> output = new HashMap<>();
        for (String itemName : renderedItems.keySet()) {
            output.put(itemName, renderedItems.get(itemName).getItemMeta().getDisplayName().toString());
        }
        return output;
    }

    private static String renderLoreLine(String text, Colors color) {
        String output = "{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"@COLOR\",\"text\":\"@TEXT\"}],\"text\":\"\"}";

        output = output.replace("@TEXT", text);
        output = output.replace("@COLOR", color.toString());
        return output;
    }
}
