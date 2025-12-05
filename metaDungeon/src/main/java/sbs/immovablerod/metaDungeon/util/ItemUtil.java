package sbs.immovablerod.metaDungeon.util;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;

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
        if (item != null && item.getType() != Material.AIR) {
            try {
                targetId = NBT.getComponents(item, nbt -> (UUID) nbt.getCompound("minecraft:custom_data").getUUID("id"));
            } catch (NullPointerException ignored) {
                return null;
            }

            for (MetaDungeonItem ele : plugin.items.values()) {
                if (ele.getId().equals(targetId)) return targetId;
            }
        }
        return null;
    }
    public static MetaDungeonItem createItem(String itemName) {
        if (itemName == null) return null;

        UUID id = UUID.randomUUID();

        MetaDungeonItem item = new MetaDungeonItem(
                itemName,
                id,
                plugin.baseItemNbts.get(itemName),
                plugin.itemsV2.path(itemName)
        );
        plugin.items.put(id, item);
        return item;
    }

}

