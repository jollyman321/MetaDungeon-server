package sbs.immovablerod.metaDungeon.managers;

import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonMonster;

import java.util.*;

import static sbs.immovablerod.metaDungeon.util.Random.getRandInt;

public class EntityManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final HashMap<UUID, MetaDungeonMonster> entities;
    private final ArrayList<UUID> killed;

    public EntityManager () {
        this.killed = new ArrayList<>();
        this.entities = new HashMap<>();
    }

    public MetaDungeonMonster add(String name, Location spawnLocation, int level) {
        if (!plugin.jsonLoader.entityTemplates.has(name)) System.out.println("unknown entity " + name);

        MetaDungeonMonster entity = new MetaDungeonMonster(plugin.jsonLoader.entityTemplates.path(name), spawnLocation, level);
        this.entities.put(entity.getEntity().getUniqueId(), entity);

        return entity;
    }
    public void kill(UUID id) {
        // removes an entity from the would
        // the id is safe for possible late use e.g weapons that function from gaining kills
        this.entities.remove(id);
        this.killed.add(id);
    }

    public boolean isDead(UUID id) {
        return this.killed.contains(id);
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
