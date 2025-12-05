package sbs.immovablerod.metaDungeon.managers;

import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;

import java.util.*;

import static sbs.immovablerod.metaDungeon.util.Random.getRandInt;

public class EntityManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final HashMap<UUID, MetaDungeonMonster> entities;

    public EntityManager () {
        this.entities = new HashMap<>();
    }

    public MetaDungeonMonster add(String name, Location spawnLocation, int level) {
        if (!plugin.entityTemplates.has(name)) System.out.println("unknown entity " + name);

        MetaDungeonMonster entity = new MetaDungeonMonster(plugin.entityTemplates.path(name), spawnLocation, level);
        this.entities.put(entity.getEntity().getUniqueId(), entity);

        return entity;
    }

    public Location getRandomLocation() {
        return plugin.map.getEntityLocations().get(getRandInt(0, plugin.map.getEntityLocations().size() - 1));
    }

    public MetaDungeonMonster getFromID(UUID id) {
        return this.entities.get(id);
    }

    public Collection<MetaDungeonMonster> getAll() {
        return this.entities.values();
    }

    public Set<UUID> getAllIDs() {
        return this.entities.keySet();
    }

    public void clear() {
        this.entities.clear();
    }
}
