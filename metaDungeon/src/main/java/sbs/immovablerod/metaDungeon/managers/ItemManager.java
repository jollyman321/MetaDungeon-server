package sbs.immovablerod.metaDungeon.managers;

import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private final MetaDungeon plugin = MetaDungeon.getInstance();

    private final List<MetaDungeonEvent> items;
    public ItemManager() {
        this.items = new ArrayList<>();
    }

    public void add(String event, int level) {
        this.items.add(new MetaDungeonEvent(event, plugin.eventTemplates.path(event), level));
    }

    public void remove(MetaDungeonEvent event) {
        this.items.remove(event);
    }

    public void clear() {
        this.items.clear();
    }
}
