package sbs.immovablerod.metaDungeon.classes.events;

import com.fasterxml.jackson.databind.JsonNode;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.util.Random;

import java.util.UUID;

import static sbs.immovablerod.metaDungeon.game.GConfig.playerManager;
import static sbs.immovablerod.metaDungeon.game.GConfig.taskManager;

public class MetaDungeonEvent {
    private final int level;
    private final JsonNode template;
    private final UUID id;
    private UUID loopTask;
    private int duration;
    private boolean completed;

    public MetaDungeonEvent(String name, JsonNode template, int level) {

        this.id = UUID.randomUUID();
        this.level = level;
        this.template = template;
        this.completed = false;
        this.duration = -1;
        this.loopTask = null;

        if (template.findValue("durationMin") != null && template.findValue("durationMax") != null) {
            this.duration = Random.getRandInt(
                    template.path("durationMin").asInt(),
                    template.path("durationMax").asInt()
            );

        }
    }

    public void onInitiated() {
        playerManager.getAll().forEach(p -> p.getPlayer().sendMessage(
                GConfig.messageManager.getMM().deserialize(this.template.at("/onInitiated/message").asText())
        ));

        if (this.duration != -1) {
            taskManager.runTaskLater(this::onCompleted, 20L * this.duration);
        }
    }

    public void onCompleted() {
        if (this.loopTask != null) {
            taskManager.cancelTask(this.loopTask);
        }
        GConfig.eventManager.remove(this);

        playerManager.getAll().forEach(p -> p.getPlayer().sendMessage(
                GConfig.messageManager.getMM().deserialize(this.template.at("/onCompleted/message").asText())
        ));

        this.completed = true;
    }

    protected void onUpdate() {
        // placeholder for children
    }

    public void startUpdateLoop(long interval) {
        this.loopTask = taskManager.runTaskTimer(this::onUpdate, interval, interval);
    }


    public boolean isCompleted() {
        return completed;
    }

    public int getLevel() {
        return level;
    }

    public JsonNode getTemplate() {
        return template;
    }


    public UUID getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }
}
