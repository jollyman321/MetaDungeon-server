package sbs.immovablerod.metaDungeon.util;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;

public class SpawnEntity {
    private final static MetaDungeon plugin = MetaDungeon.getInstance();
    public static MetaDungeonMonster spawn(String name, Location spawnLocation, int level) {
        if (!plugin.entityTemplates.has(name)) System.out.println("unknown entity " + name);

        MetaDungeonMonster entity = new MetaDungeonMonster(plugin.entityTemplates.path(name), spawnLocation, level);
        plugin.entities.put(entity.getEntity().getUniqueId(), entity);

        return entity;
    }
}
