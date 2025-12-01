package sbs.immovablerod.metaDungeon.util;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class Serialize
{
    public static String serializeItem(ItemStack item) {
        return NBT.itemStackToNBT(item).toString();
    }
    public static ItemStack deserializeItem(String item) {
        return NBT.itemStackFromNBT(NBT.parseNBT(item));
    }
    public static ReadWriteNBT serializeEntity(Entity entity) {
        ReadWriteNBT entityNbt = NBT.createNBTObject();
        NBT.get(entity, entityNbt::mergeCompound);
        return entityNbt;
    }
    public static Entity deserializeEntity(Entity entity, ReadWriteNBT nbtData) {
        ReadWriteNBT entityNbt = NBT.createNBTObject();
        NBT.modify(entity, nbt -> {
            nbt.mergeCompound(entityNbt);
        });

        return entity;
    }
}
