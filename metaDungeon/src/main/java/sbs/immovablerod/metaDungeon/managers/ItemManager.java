package sbs.immovablerod.metaDungeon.managers;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonItem;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonPlayer;
import sbs.immovablerod.metaDungeon.util.RenderItemsV2;


import java.util.HashMap;
import java.util.UUID;

public class ItemManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final HashMap<UUID, MetaDungeonItem> items;
    public HashMap<String, ReadWriteNBT> baseItemNBTs;
    public ItemManager() {
        this.items = new HashMap<>();
        this.baseItemNBTs  = RenderItemsV2.renderBaseItemNbt(plugin.jsonLoader.itemsV2);
    }

    public MetaDungeonItem add(String itemName, MetaDungeonPlayer owner) {
        if (itemName == null) return null;

        UUID id = UUID.randomUUID();
        try {
            MetaDungeonItem item = new MetaDungeonItem(
                    itemName,
                    owner,
                    id,
                    this.baseItemNBTs.get(itemName),
                    plugin.jsonLoader.itemsV2.path(itemName)
            );
            this.items.put(id, item);
            return item;
        } catch (NullPointerException e) {
            plugin.getLogger().warning("Could Not Create Item " + itemName);
            plugin.getLogger().warning(e.toString());
            return null;
        }
    }
    public UUID getItemId(ItemStack item) {
        UUID targetId;
        if (item != null && item.getType() != Material.AIR) {
            try {
                targetId = NBT.getComponents(item, nbt -> (UUID) nbt.getCompound("minecraft:custom_data").getUUID("id"));
            } catch (NullPointerException ignored) {
                return null;
            }

            for (MetaDungeonItem ele : this.items.values()) {
                if (ele.getId().equals(targetId)) return targetId;
            }
        }
        return null;
    }

    public MetaDungeonItem getAdvancedItem(ItemStack item){
        UUID id = this.getItemId(item);
        if (id != null) {
            return this.items.get(id);
        }
        return null;
    }

    public void clear() {
        this.items.clear();
    }
}
