package sbs.immovablerod.metaDungeon.util;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;

public class SpawnEntity {
    private final static MetaDungeon plugin = MetaDungeon.getInstance();
    public static MetaDungeonMonster spawn(String name, Location spawnLocation) {
        if (!plugin.entitiesDB.containsKey(name)) System.out.println("unknown entity " + name);

        MetaDungeonMonster entity = new MetaDungeonMonster(
                EntityType.valueOf(plugin.entitiesDB.get(name).get("type")),
                spawnLocation,
                Integer.parseInt(plugin.entitiesDB.get(name).get("health")),
                Integer.parseInt(plugin.entitiesDB.get(name).get("damage")),
                Integer.parseInt(plugin.entitiesDB.get(name).get("movement")),
                Integer.parseInt(plugin.entitiesDB.get(name).get("defence")),

                plugin.entitiesDB.get(name).get("mainHand"),
                plugin.entitiesDB.get(name).get("offHand"),
                plugin.entitiesDB.get(name).get("helmet"),
                plugin.entitiesDB.get(name).get("chestplate"),
                plugin.entitiesDB.get(name).get("leggings"),
                plugin.entitiesDB.get(name).get("boots")
        );
        plugin.entities.put(entity.getEntity().getUniqueId(), entity);

        return entity;
    }
}
