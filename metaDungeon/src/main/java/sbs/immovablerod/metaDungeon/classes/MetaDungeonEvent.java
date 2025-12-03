package sbs.immovablerod.metaDungeon.classes;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.elements.EventInterface;
import sbs.immovablerod.metaDungeon.elements.ItemInterface;
import sbs.immovablerod.metaDungeon.enums.Events;
import sbs.immovablerod.metaDungeon.enums.Items;

public class MetaDungeonEvent {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final EventInterface controller;
    private final int level;

    public MetaDungeonEvent(String name, JsonNode template, int level) {
        this.level = level;


        // implement logic functions
        this.controller = Events.get(name, this);
    }
}
