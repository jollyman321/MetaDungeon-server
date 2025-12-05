package sbs.immovablerod.metaDungeon.classes;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.MetaDungeon;
import sbs.immovablerod.metaDungeon.elements.EventInterface;
import sbs.immovablerod.metaDungeon.enums.Events;
import sbs.immovablerod.metaDungeon.game.GConfig;

public class MetaDungeonEvent {
    private final MetaDungeon plugin = MetaDungeon.getInstance();
    private final EventInterface controller;
    private final int level;
    private final JsonNode template;
    private boolean completed;

    public MetaDungeonEvent(String name, JsonNode template, int level) {
        this.level = level;
        this.template = template;
        this.completed = false;
        // implement logic functions
        this.controller = Events.get(template.path("controller").asText(), this);

        this.controller.onInitiated();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void end() {
        this.completed = true;
        this.controller.onCompleted();
        GConfig.eventManager.remove(this);
    }

    public int getLevel() {
        return level;
    }

    public JsonNode getTemplate() {
        return template;
    }
}
