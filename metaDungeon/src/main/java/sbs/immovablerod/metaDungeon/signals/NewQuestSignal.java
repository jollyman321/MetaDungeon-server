package sbs.immovablerod.metaDungeon.signals;

import org.bukkit.Location;
import sbs.immovablerod.metaDungeon.classes.events.MetaDungeonEvent;
import sbs.immovablerod.metaDungeon.classes.events.QuestEvent;

public class NewQuestSignal extends BaseSignal {

    private final QuestEvent event;

    public NewQuestSignal(QuestEvent event) {
        super();
        this.event = event;
    }

    public QuestEvent getEvent() {
        return event;
    }
}
