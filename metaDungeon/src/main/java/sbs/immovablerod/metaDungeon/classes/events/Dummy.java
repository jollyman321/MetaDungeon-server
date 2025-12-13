package sbs.immovablerod.metaDungeon.classes.events;

import com.fasterxml.jackson.databind.JsonNode;

public class Dummy extends MetaDungeonEvent {
    public Dummy(String name, JsonNode template, int level) {
        super(name, template, level);
    }

}
