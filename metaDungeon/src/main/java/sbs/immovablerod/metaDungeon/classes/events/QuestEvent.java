package sbs.immovablerod.metaDungeon.classes.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.classes.MetaDungeonEffect;
import sbs.immovablerod.metaDungeon.enums.Effects;
import sbs.immovablerod.metaDungeon.enums.Signal;
import sbs.immovablerod.metaDungeon.game.GConfig;
import sbs.immovablerod.metaDungeon.signals.NewQuestSignal;

public class QuestEvent extends MetaDungeonEvent {
    private Location questLocation;
    public QuestEvent(String name, JsonNode template, int level) {
        super(name, template, level);


    }
    @Override
    public void onInitiated() {
        super.onInitiated();
        GConfig.signalListeners.get(Signal.NEW_QUEST).trigger(new NewQuestSignal(this));
    }

    public void setQuestLocation(Location questLocation) {
        this.questLocation = questLocation;
    }

    public Location getQuestLocation() {
        return questLocation;
    }
}
