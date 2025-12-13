package sbs.immovablerod.metaDungeon.managers;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.events.MetaDungeonEvent;
import sbs.immovablerod.metaDungeon.enums.Event;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final List<MetaDungeonEvent> events;
    public EventManager() {
        this.events = new ArrayList<>();
    }

    public void add(String name, int level) {
        MetaDungeonEvent event = Event.get(name, plugin.jsonLoader.eventTemplates.path(name), level);
        if (event != null) {
            this.events.add(event);
            event.onInitiated();
        } else {
            plugin.getLogger().warning("Could not add event '" + name + "' (reason=does not exist)");
        }
    }

    public void remove(MetaDungeonEvent event) {
        this.events.remove(event);
    }

    public List<MetaDungeonEvent> getAll() {
        return this.events;
    }

    public void clear() {
        this.events.clear();
    }
}
