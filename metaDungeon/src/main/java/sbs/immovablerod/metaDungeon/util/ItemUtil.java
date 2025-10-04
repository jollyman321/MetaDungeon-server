package sbs.immovablerod.metaDungeon.util;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;

import java.util.UUID;

public class ItemUtil {
    private static final MetaDungeon plugin = MetaDungeon.getInstance();

    public static MetaDungeonItem getAdvancedItem(ItemStack item){
        UUID id = getItemId(item);
        if (id != null) {
            return plugin.items.get(id);
        }
        return null;
    }
    public static UUID getItemId(ItemStack item) {
        UUID targetId;
        try {
            targetId = NBT.get(item, nbt -> (UUID) nbt.getUUID("id"));
        } catch (NullPointerException ignored) {return null;}

        for (MetaDungeonItem ele : plugin.items.values()) {
            if (ele.getId().equals(targetId)) return targetId;
        }

        return null;
    }
    public static MetaDungeonItem createItem(String itemName) {
        if (itemName == null) return null;

        String type;
        if (plugin.itemsDB.get(itemName).get("type") == null) {
            type = "BARRIER";
        } else {
            type = plugin.itemsDB.get(itemName).get("type");
        }
        UUID id = UUID.randomUUID();

        MetaDungeonItem item = new MetaDungeonItem(
                itemName,
                id,
                plugin.baseItemNbts.get(itemName),
                type,
                plugin.itemsDB.get(itemName).get("category"),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("tier")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("rarity")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("cost")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("consumable")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("randomGen")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("damage")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("defence")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("movement")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("health")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("stamina")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("weight")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("heal_life")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("heal_stamina")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("durability")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("attackSpeed")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("staminaCost")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("knockback")),
                Integer.parseInt(plugin.itemsDB.get(itemName).get("damagePercent"))


        );
        plugin.items.put(id, item);
        return item;
    }
}

