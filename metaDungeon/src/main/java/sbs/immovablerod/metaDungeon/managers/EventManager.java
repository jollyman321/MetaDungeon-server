package sbs.immovablerod.metaDungeon.managers;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEvent;
import sbs.immovablerod.metaDungeon.enums.Events;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final List<MetaDungeonEvent> events;
    public EventManager() {
        this.events = new ArrayList<>();
    }

    public void add(String event, int level) {
        this.events.add(new MetaDungeonEvent(event, plugin.jsonLoader.eventTemplates.path(event), level));
    }

    public void remove(MetaDungeonEvent event) {
        this.events.remove(event);
    }

    public void clear() {
        this.events.clear();
    }
}
